package com.zbk.servlet;

import com.zbk.annaotation.*;
import com.zbk.controller.UserController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Dispatcher servlet.
 * 1、init初始化。
 * 2、把方法和地址关联
 *
 * @author 张卜亢
 * @date 2019.04.18 16:25:35
 */
public class DispatcherServlet extends HttpServlet {

    //存放扫描到的类列表
    List<String> classNames = new ArrayList<String>();

    //存放实现IOC的容器
    Map<String, Object> beans = new HashMap<String, Object>();

    //存放请求地址和方法的对应关系
    Map<String, Object> hardlerMap = new HashMap<String, Object>();

    @Override
    public void init(ServletConfig config) {
        //把所有的bean扫描出来
        scanPackage("com.zbk");//这个包可以写到properties配置文件

        //根据扫描出来的list全类名，进行实例化创建bean
        doInstance();

        //把依赖注入
        doIoc();

        //把方法和请求地址做映射关联
        buildUrlMapping();
    }

    //把所有的bean扫描出来
    private void scanPackage(String basePackage) {
        //获取到指定的包，将点替换成斜杠
        URL url = this.getClass().getClassLoader()
                .getResource("/" + basePackage.replaceAll("\\.", "/"));
        //拿到包下的文件，子文件递归查找，然后保存起来
        String fileStr = url.getFile();
        File file = new File(fileStr);
        String[] filesStr = file.list();
        for (String path : filesStr) {
            File f = new File(fileStr + path);
            if (f.isDirectory()) {
                scanPackage(basePackage + "." + path);
            } else {
                //加入lins中
                classNames.add(basePackage + "." + f.getName()); //得到com.xxx...样式的
            }
        }
    }

    //根据扫描出来的list全类名，进行实例化
    private void doInstance() {
        if (classNames.size() <= 0) {
            System.out.println("扫描失败，className列表为空");
            return;
        }

        //list的所有class类，对这些类进行实例化
        for (String className : classNames) {
            String cn = className.replace(".class", "");
            try {
                Class<?> clazz = Class.forName(cn);

                if (clazz.isAnnotationPresent(ZbkController.class)) {
                    Object instance = clazz.newInstance(); //创建控制类

                    ZbkRequestMapping requestMapping = clazz.getAnnotation(ZbkRequestMapping.class);
                    String rmvalue = requestMapping.value(); //拿到了路径
                    beans.put(rmvalue, instance);//将映射关系放到容器中
                } else if (clazz.isAnnotationPresent(ZbkService.class)) {
                    ZbkService service = clazz.getAnnotation(ZbkService.class);
                    Object instance = clazz.newInstance();
                    beans.put(service.value(), instance);
                } else {
                    continue;
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    //把service注入到controller
    private void doIoc() {
        if (beans.entrySet().size() <= 0) {
            System.out.println("没有一个实例化的类");
            return;
        }

        //把map哩所有的实例便利出来
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object instance = entry.getValue();
            Class<?> clazz = instance.getClass();
            //判断是不是controller，这个不是必须的，因为这里只有controller用到了所以做判断，可以不判断
            if (clazz.isAnnotationPresent(ZbkController.class)) {
                //拿到属性列表
                Field[] fields = clazz.getDeclaredFields();
                //循环判断是否需要注入
                for (Field field : fields) {
                    if (field.isAnnotationPresent(ZbkAutowired.class)) {
                        ZbkAutowired autowired = field.getAnnotation(ZbkAutowired.class);
                        String key = autowired.value(); //拿到key来从map中拿实例，如果没有key的话spring是把属性名作为key
                        field.setAccessible(true);//因为该属性是private的，所以需要设置一下，否则没有权限，会注入不进去
                        try {
                            field.set(instance, beans.get(key));//注入属性
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                continue;
            }
        }
    }

    private void buildUrlMapping() {
        if (beans.entrySet().size() <= 0) {
            System.out.println("没有类的实例化");
            return;
        }
        //遍历beans
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object instance = entry.getValue();
            Class<?> clazz = instance.getClass();
            //判断是不是controller类，因为只有controller类中使用
            if (clazz.isAnnotationPresent(ZbkController.class)) {
                ZbkRequestMapping requestMapping = clazz.getAnnotation(ZbkRequestMapping.class);
                String classPath = requestMapping.value();//获取类上的请求路径
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(ZbkRequestMapping.class)) {
                        ZbkRequestMapping methodMapping = method.getAnnotation(ZbkRequestMapping.class);
                        String methodPath = methodMapping.value();//获取方法上的请求路径
                        //classPath+methodPath 形成完全访问路径
                        hardlerMap.put(classPath + methodPath, method);//将路径和方法对应关系放入map
                    } else {
                        continue;
                    }
                }
            } else {
                continue;
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求路径 /xxx/query
        String uri = req.getRequestURI(); //得到的是 /tomcat中war路径/xxx/query
        String context = req.getContextPath(); //得到/tomcat中war路径
        String path = uri.replace(context, ""); //得到/xxx/query

        //根据路径拿方法
        Method method = (Method) hardlerMap.get(path);

        //根据key，类上的请求路径，从map中获取实现
        Object instance = beans.get("/" + path.split("/")[1]); //key之前保存的事/xxx，类上的请求路径

        //对参数进行处理，springmvc使用的策略模式实现的，这里使用简略的非策略模式
        Object[] arg = hand(req, resp, method);
        try {
            method.invoke(instance, arg);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    //对controller中的请求方法参数进行处理，springmvc底层用的策略模式，这里使用非策略模式写的
    private static Object[] hand(HttpServletRequest request, HttpServletResponse response, Method method) {
        //拿到当前带执行的方法有那些参数
        Class<?>[] paramClazzs = method.getParameterTypes();
        //根据参数的个数，new一个参数的数组，将方法哩的所有参数赋值到args来
        Object[] args = new Object[paramClazzs.length];
        int args_i = 0;
        int index = 0;
        for (Class<?> paramClazz : paramClazzs) {
            if (ServletRequest.class.isAssignableFrom(paramClazz)) {
                args[args_i++] = request;
            }
            if (ServletResponse.class.isAssignableFrom(paramClazz)) {
                args[args_i++] = response;
            }
            /*
            从0-3判断有没有RequestParam注解，很明显paramClazz为0和1时，不是，
            当为2和3时为@RequestParam，需要解析
            [@com.zbk.myspringmvc.annotation.ZbkRequestParam(value=name)]
             */
            Annotation[] paramAns = method.getParameterAnnotations()[index];
            if (paramAns.length > 0) {
                for (Annotation paramAn : paramAns) {
                    if (ZbkRequestParam.class.isAssignableFrom(paramAn.getClass())) {
                        ZbkRequestParam rp = (ZbkRequestParam) paramAn;
                        //找到注解哩的name和age
                        args[args_i++] = request.getParameter(rp.value());
                    }
                }
            }
            index++;

        }
        return args;
    }
}

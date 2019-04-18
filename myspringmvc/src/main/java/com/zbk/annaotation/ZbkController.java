package com.zbk.annaotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE}) //TYPE表示该注解适用于类上
@Retention(RetentionPolicy.RUNTIME) //RUNTIME表示在运行时可以使用反射机制获取注解
@Documented //注解被包含在javadoc中，扩展：注解可以被继承，但需要添加一个@Inherited
public @interface ZbkController {
    String value() default ""; //用来接收使用注解时传入的value值，@Xxx("abc")中的abc
}

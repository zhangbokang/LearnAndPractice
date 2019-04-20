package com.zbk.wsboot.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 聊天类，也就是处理websocket请求的类
 *
 * @author 张卜亢
 * @date 2019.04.21 01:27:45
 */
@Component
@ServerEndpoint("/websocket") //客户端连接的url
public class ChatWebSocket {
    //concurrent包的线程安全集合
    //用来存放每个客户端对应的chatwebsocket对象
    private static CopyOnWriteArrayList<ChatWebSocket> chatWebSockets = new CopyOnWriteArrayList<>();
    //与某个客户端的连接会话对象，通过session给客户端发送数据
    private Session session;

    /**
     * 连接建立成功回调
     *
     * @param session the session
     * @author 张卜亢
     * @date 2019.04.21 01:32:35
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        chatWebSockets.add(this);
        System.out.println("新链接加入，当前连接数：" + chatWebSockets.size());
        sendInfo("新链接加入，当前连接数：" + chatWebSockets.size());
        //发送消息
        try {
            sendMessage("当前共有用户：" + chatWebSockets.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 收到客户端消息的回调
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息：" + message);
        sendInfo(message);
    }

    /**
     * 连接关闭的回调
     */
    @OnClose
    public void onClose() {
        chatWebSockets.remove(this);
        sendInfo("有一个连接关闭，当前在线：" + chatWebSockets.size());
        System.out.println("有一个连接关闭，当前连接数：" + chatWebSockets.size());
    }

    /**
     * 发生错误的回调
     *
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("连接发生错误");
        throwable.printStackTrace();
    }

    /**
     * 通过session会话发送消息
     *
     * @param msg
     */
    private void sendMessage(String msg) throws IOException {
        this.session.getBasicRemote().sendText(msg);
    }

    /**
     * 群发消息
     *
     * @param msg
     */
    private void sendInfo(String msg) {
        for (ChatWebSocket webSocket : chatWebSockets) {
            //把异常处理放到for里面，当一个用户发生异常后，继续发送消息到后面的用户
            try {
                webSocket.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

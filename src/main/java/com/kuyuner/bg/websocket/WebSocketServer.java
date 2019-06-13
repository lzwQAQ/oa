package com.kuyuner.bg.websocket;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by Administrator on 2018/3/25.
 */

//@Slf4j
@ServerEndpoint(value = "/websocket")
@Component
public class WebSocketServer {

    Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
// 备用的同步hashmap
//private static ConcurrentHashMap<Session,WebSocketServer> webSocketServerConcurrentHashMap = new ConcurrentHashMap();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this); //加入set中
        addOnlineCount(); //在线数加1
        log.info("有新连接加入！当前在线人数为" + getOnlineCount());
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("websocket IO异常");
        }
    }
// //连接打开时执行
// @OnOpen
// public void onOpen(@PathParam("user") String user, Session session) {
// currentUser = user;
// System.out.println("Connected ... " + session.getId());
// }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this); //从set中删除
        subOnlineCount(); //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("来自客户端的消息:" + message);

//群发消息
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage("response message:"+message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//单发的信息
        sendMessage("currentWebsocket is ok!!! ");
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message) throws IOException {
//log.info(message);
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }


//    @RequestMapping(value="/publicSend",method= RequestMethod.GET)
//    @ResponseBody
//    public String pushVideoListToWeb2(String id) {
//        try {
//            WebSocketServer.sendInfo("有新客户呼入,sltAccountId:"+id);
//            for(int i= 0;i<20;i++){
//                WebSocketServer.sendInfo("有新客户呼入,testing:"+String.valueOf(i));
//            }
//        }catch (IOException e) {
//
//        }
//        return "success!";
//
//    }
}
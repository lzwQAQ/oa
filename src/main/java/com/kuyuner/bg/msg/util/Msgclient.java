package com.kuyuner.bg.msg.util;

import com.linkage.netmsg.NetMsgclient;
import com.linkage.netmsg.server.ReceiveMsg;

public class Msgclient {
    private static NetMsgclient client = null;

    public static synchronized NetMsgclient getClient() {
        //判断存储实例的变量是否有值
        if (client == null || client.isStoped) {
            //如果没有，就创建一个类实例，并把值赋值给存储类实例的变量
            ReceiveMsg receiveMsg = new ReceiveMessage();
            client   = new NetMsgclient();
            client = client.initParameters("202.102.41.101", 9005, "0515C00162703", "123456", receiveMsg);
            try {
                /*登录认证*/
                boolean isLogin = client.anthenMsg(client);
                if(isLogin) {
                    System.out.println("message login sucess");
                }else {
                    System.out.println("message login error");
                    client.closeConn();
                    client = null;
                }
            } catch (Exception e1) {
                throw new RuntimeException();
            }
        }
        return client;
    }

    public static void setClient(NetMsgclient client) {
        Msgclient.client = client;
    }
}

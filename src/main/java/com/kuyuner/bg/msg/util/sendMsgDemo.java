package com.kuyuner.bg.msg.util;

import com.linkage.netmsg.NetMsgclient;
import com.linkage.netmsg.server.ReceiveMsg;

public class sendMsgDemo {
    public static void main(String[] args) {
        NetMsgclient client   = new NetMsgclient();
        /*ReceiveMsgImpl为ReceiveMsg类的子类，构造时，构造自己继承的子类就行*/
        ReceiveMsg receiveMsg = new ReceiveMessage();
        /*初始化参数*/
        client = client.initParameters("202.102.41.101", 9005, "0515C00162703", "123456", receiveMsg);
        try {
            /*登录认证*/
            boolean isLogin = client.anthenMsg(client);
            if(isLogin) {
                System.out.println("login sucess");
                /*发送下行短信*/
                System.out.println(client.sendMsg(client, 0, "18861336296", "大逼哥吃大鱼", 1));//17372322116
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}

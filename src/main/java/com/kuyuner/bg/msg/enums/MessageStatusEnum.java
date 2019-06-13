package com.kuyuner.bg.msg.enums;

public enum MessageStatusEnum {
//  AnswerBean类的status属性
    SUCCESS(0, "成功"),
    SYSTEM_ERROR(1,"系统错误"),
    ACCOUNT_ERROR(2, "账号错误"),
    SECRET_ERROR(3, "密码错误"),
    CONNECTION_OUTER(4, "连接数超过限制"),
    SEND_ITEMS_OUTER(5, "秒发送条目数超过限制"),
    TARGET_PHONE_LIMIT(6, "目的号码受限制"),
    WEB_ERROR(7, "网络错误"),
    SEND_TIMES_OUTER(8, "月发送条目数超过限制"),
    CLIENT_CLOSE(9, "客户端关闭连接"),
    MESSAGE_GATEWAY_CLOSE(10, "短信网关关闭连接"),
    TIME_OUT(11, "超时退出"),
    DATA_BASE_ERROR(12, "连接数据库错误"),
    CONNECT_MESSAGE_GATEWAY_ERROR(13, "连接短信网关错误"),
    ERROR_SEND_CONTENT(14, "非法发送内容"),
    ERROR_SEND_TIME(15, "非法发送时间"),
    ACCOUNT_EXCEPTION(16, "账号异常,socket连接已经关闭"),
    SEND_PHONE_ERROR(23, "发送号码前缀错误");


    private int code;
    private String remark;

    MessageStatusEnum(int code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public int getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
    }


    public static String getValueByCode(int code){
        for(MessageStatusEnum messageStatusEnum:MessageStatusEnum.values()){
            if(code==messageStatusEnum.getCode()){
                return messageStatusEnum.getRemark();
            }
        }
        return  null;
    }
}

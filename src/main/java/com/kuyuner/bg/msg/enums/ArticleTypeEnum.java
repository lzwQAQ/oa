package com.kuyuner.bg.msg.enums;

public enum ArticleTypeEnum {
    ROTATE_PICTURE(1, "轮播图"),
    PUBLIC_ANNOUNCEMENT(2,"单位公告"),
    SPECIAL_TOPIC(3, "重点专题"),
    JOURNAL_SHARE(4, "期刊分享"),
    STUDY_AND_DO(5, "两学一做"),
    BUILD_HARMONIOUS(6, "共建和谐"),
    PARTY_AFFAIRS(7, "党务文献"),
    KNOWLEDGE(8, "专业知识");


    private int code;
    private String remark;

    ArticleTypeEnum(int code, String remark) {
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
        for(ArticleTypeEnum messageStatusEnum: ArticleTypeEnum.values()){
            if(code==messageStatusEnum.getCode()){
                return messageStatusEnum.getRemark();
            }
        }
        return  null;
    }
}

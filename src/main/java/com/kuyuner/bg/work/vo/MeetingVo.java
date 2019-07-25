package com.kuyuner.bg.work.vo;

import com.kuyuner.core.common.dict.DictType;
import com.kuyuner.core.sys.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 会议通知实体
 *
 * @author administrator
 */
@Getter
@Setter
public class MeetingVo {

    /**
     * 会议主题
     */
    private String title;

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 会议时长
     */
    @NotNull(message = "会议时长不能为空")
    private String duration;

    /**
     * 会议地点
     */
    @NotNull(message = "会议地点不能为空")
    @Length(max = 200, message = "会议地点长度不能大于200")
    private String place;

    /**
     * 会议发起人
     */
    @NotNull(message = "会议发起人不能为空")
    @Length(max = 32, message = "会议发起人长度不能大于32")
    private User trainer;

    /**
     * 参与人员
     */
    @Length(max = 4000, message = "参与人员长度不能大于2000")
    private String joinPeople;

    /**
     * 会议内容
     */
    @NotNull(message = "会议内容不能为空")
    @Length(max = 2000, message = "会议内容长度不能大于2000")
    private String content;

    /**
     * 备注
     */
    @Length(max = 2000, message = "备注长度不能大于2000")
    private String marks;

    /**
     * 1：表示发送；0：表示接受
     */
    @NotNull(message = "1：表示发送；0：表示接受不能为空")
    @Length(max = 1, message = "1：表示发送；0：表示接受长度不能大于1")
    private String meetingType;

    /**
     * 1：发送短信；0：无短信
     */
    @NotNull(message = "1：发送短信；0：无短信不能为空")
    @Length(max = 1, message = "1：发送短信；0：无短信长度不能大于1")
    private String isSendMessage;

    /**
     * 会议类型
     */
    @NotNull(message = "会议类型不能为空")
    @Length(max = 5, message = "会议类型长度不能大于5")
    @DictType("MEETING_TYPE")
    private String type;

    private String sender;
}
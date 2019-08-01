package com.kuyuner.bg.approval.controller;

import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.core.sys.service.UserService;

import java.util.List;

public class SequenceFlowNameUtil {
    public static String getSequenceFlowName(String userId, String sequenceFlowName, UserService userService){
        String uid = UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId();
        String str = sequenceFlowName;
        if(StringUtils.isNotBlank(uid) && StringUtils.isBlank(sequenceFlowName) || "null".equals(sequenceFlowName)){
            List<String> list = userService.getRoleName(uid);
            for (String s : list) {
                if ("办公室主任".equals(s)) {
                    str = s;
                    break;
                }
                str =  "非办公室主任";
            }
        }
        return str;
    }
}

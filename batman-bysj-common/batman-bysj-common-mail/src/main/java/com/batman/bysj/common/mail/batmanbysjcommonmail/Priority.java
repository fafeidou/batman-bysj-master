package com.batman.bysj.common.mail.batmanbysjcommonmail;

/** 
* 表示邮件的优先级（重要程度），对应 X-Priority 属性
* @author victor.qin 
* @date 2018/4/20 15:21
*/ 
public enum Priority {
    Low("5"),
    Normal("3"),
    High("1");

    private String value;

    Priority(String value) {
        this.value = value;
    }

    String value() {
        return this.value;
    }
}

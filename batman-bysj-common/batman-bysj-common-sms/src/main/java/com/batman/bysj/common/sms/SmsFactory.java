package com.batman.bysj.common.sms;

import com.batman.bysj.common.sms.config.SmsConfig;
import com.batman.bysj.common.sms.constants.SmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author victor.qin
 * @date 2018/5/9 13:00
 */
@Component
public class SmsFactory {

    @Autowired
    private SmsConfig smsConfig;

    public SmsService build() {
        if (smsConfig.getType().equals(SmsConstants.TYPE_ALi)) {
            return new ALiSmsService(smsConfig);
        }
        return null;
    }
}

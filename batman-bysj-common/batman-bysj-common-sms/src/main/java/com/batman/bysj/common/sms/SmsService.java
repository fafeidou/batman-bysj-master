package com.batman.bysj.common.sms;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.batman.bysj.common.sms.config.SmsConfig;

/**
 * @author victor.qin
 * @date 2018/5/9 13:04
 */
public abstract class SmsService {
    protected SmsConfig smsConfig;

    /**
     * 发送短信
     * @return
     */
    abstract SendSmsResponse sendSms(String phoneNumber) throws ClientException;

    /**
     * 查询短信
     * @param bizId
     * @return
     */
    abstract QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException;
}

package com.batman.bysj.common.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * redis自动生成主键
 *
 * @author victor.qin
 * @date 2018/5/23 11:20
 */
@Service
public class TransactionId {
    private final RedisTemplate<String, String> redisTemplate;


    public TransactionId(final RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 商户传入的业务流水号。此字段由商户生成，需确保唯一性，用于定位每一次请求，后续按此流水进行对帐。
     * 生成规则: 固定30位数字串，前17位为精确到毫秒的时间yyyyMMddhhmmssSSS，后13位为自增数字。
     */
    public String nextTransactionId() {
        Long autoIncr = redisTemplate.opsForValue().increment("liking_wear:zmxy:transaction_id", 1);
        String autoIncrStr = StringUtils.leftPad(String.valueOf(autoIncr), 13, "0");
        return DateTimeFormatter.ofPattern("yyyyMMddhhmmssSSS").format(LocalDateTime.now()) + autoIncrStr;
    }
}

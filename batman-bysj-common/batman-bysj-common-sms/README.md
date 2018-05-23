#发送短信过时校验方法
1、首先查下redis是否过期只有过期才能重新发送
if (redisTemplate.opsForValue().get(LIKINGWEAR_MP_SMSCODE_PREFIX + sessionId + ":count_down") != null) {
            return "发送短信过于频繁, 请稍后再试";
 }
 2、
 //code有十分钟有效期
             redisTemplate.opsForValue().set(LIKINGWEAR_MP_SMSCODE_PREFIX + sessionId,code,10,TimeUnit.MINUTES);
 // 一个号码一分钟只能发送一次
 redisTemplate.opsForValue().set(LIKINGWEAR_MP_SMSCODE_PREFIX + sessionId+":count_down",code,1,TimeUnit.MINUTES);
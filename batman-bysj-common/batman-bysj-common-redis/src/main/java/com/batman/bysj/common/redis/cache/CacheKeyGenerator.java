package com.batman.bysj.common.redis.cache;

import com.batman.bysj.common.util.JacksonUtils;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * CacheKeyGenerator
 *
 * @author aooer 2016/3/16.
 * @version 2.0.0
 * @since 2.0.0
 */
public class CacheKeyGenerator implements KeyGenerator {

    private static final String PRE_STR = "DATA_CACHE_";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        if (params == null) {
            return String.format("%s%s_%s", PRE_STR, method.getName(), method.getDeclaringClass().getSimpleName());
        } else {
            String paramstr;
            if (params.length == 1) {
                paramstr = JacksonUtils.bean2Json(params[0]);
            } else {
                paramstr = JacksonUtils.bean2Json(params);
            }
            paramstr = paramstr.replaceAll("\"", "'");
            return String.format("%s%s_%s[%s]", PRE_STR, method.getName(), method.getDeclaringClass().getSimpleName(), paramstr);
        }
    }
}

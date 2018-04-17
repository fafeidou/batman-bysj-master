package com.batman.bysj.provider.api.hystrix;

import com.batman.bysj.provider.api.TestFeignApi;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author victor.qin
 * @date 2018/4/3 15:14
 */
@Component
public class TestFeignHystrix implements TestFeignApi {
    @Override
    public ResponseEntity<byte []> test() {
        return null;
    }
}

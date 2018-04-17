package com.batman.bysj.provider.api;

import com.batman.bysj.provider.api.hystrix.TestFeignHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;


/**
 * @author victor.qin
 * @date 2018/4/3 15:09
 */
@FeignClient(value = "TestFeignApi",fallback = TestFeignHystrix.class)
public interface TestFeignApi {
    /**
     * 测试
     * @return 返回字符串
     */
    @GetMapping(value = "/test")
    ResponseEntity<byte []> test() throws IOException;
}

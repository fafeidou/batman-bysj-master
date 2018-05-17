package com.bysj.provider.teacher.service;

import com.batman.bysj.provider.api.TestFeignApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author victor.qin
 * @date 2018/4/3 15:29
 */

@RestController
public class TestFeignClient implements TestFeignApi{
    @Override
    public ResponseEntity<byte[]> test() throws IOException {
        return null;
    }
//    @Autowired
//    private TestService testService;

}

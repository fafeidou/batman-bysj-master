package com.bysj.provider.teacher.service;

import com.batman.bysj.provider.api.TestFeignApi;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author victor.qin
 * @date 2018/4/3 15:29
 */

@RestController
public class TestFeignClient implements TestFeignApi{
//    @Autowired
//    private TestService testService;
    @Override
    public String test() {
//        List<Test> tests = testService.selectAll();
        return "sfsfsf";
//        return tests.toString();
    }
}

package com.bysj.provider.teacher.service.web;

import com.batman.bysj.common.model.Test;
import com.batman.bysj.common.service.TestService;
import com.batman.bysj.provider.api.TestFeignApi;
import com.bysj.common.util.AbstractListExcelGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

/**
 * @author victor.qin
 * @date 2018/4/3 15:29
 */

@RestController
public class TestFeignClient extends  BaseController implements TestFeignApi{
    @Autowired
    private TestService testService;
    @Override
    public ResponseEntity<byte []> test() throws IOException {
        // 实现对应接口
        AbstractListExcelGenerator<Map<String, String>, Object> generator =
                new AbstractListExcelGenerator<Map<String, String>, Object>() {
                    @Override
                    public String title() {
                        return "标题";
                    }
                    @Override
                    protected List<Map<String, String>> data(List<String> fields, Object o) {
                        HashMap<String, String> a = new HashMap<>();
                        a.put("key1", "数据1-1");
                        a.put("key2", "数据1-2");
                        HashMap<String, String> b = new HashMap<>();
                        b.put("key1", "数据2-1");
                        b.put("key2", "数据2-2");
                        HashMap<String, String> c = new HashMap<>();
                        c.put("key1", "数据3-1");
                        c.put("key2", "数据3-1");
                        HashMap<String, String> d = new HashMap<>();
                        d.put("key1", "数据4-1");
                        d.put("key2", "数据4-2");
                        return Arrays.asList(a, b, c, d);
                    }
                    @Override
                    protected LinkedHashMap<String, String> sortedFieldDict(List<String> fields) {
                        LinkedHashMap<String, String> test = new LinkedHashMap<>();
                        test.put("key1", "表头1");
                        test.put("key2", "表头2");
                        return test;
                    }
                };
        byte[] bytes = generator.generateExcelFile(null, null);
        return  toResponse("测试excel" + ".xlsx",
                bytes);
    }
}

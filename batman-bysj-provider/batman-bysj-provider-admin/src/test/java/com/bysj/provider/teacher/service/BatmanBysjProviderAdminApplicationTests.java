package com.bysj.provider.teacher.service;

import com.batman.bysj.common.dao.mapper.TestMapper;
import com.batman.bysj.common.service.TestService;
import com.bysj.common.util.AbstractListExcelGenerator;
import com.github.pagehelper.PageHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BatmanBysjProviderAdminApplicationTests {
    @Autowired
    private TestMapper testMapper;
    @Autowired
    private TestService testService;
    @Test
    public void contextLoads() {

        PageHelper.startPage(1, 10);
        List<com.batman.bysj.common.model.Test> tests1 = testService.selectByExample(new Example(com.batman.bysj.common.model.Test.class));

        tests1.forEach(i -> {
            System.out.println(i.toString());
        });
    }

    @Test
    public void listExcelGenerateTest() throws IOException {
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
    }
}

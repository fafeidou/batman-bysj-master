package com.batman.bysj.mongo;

import com.batman.bysj.mongo.dao.BaseMongoDao;
import com.batman.bysj.mongo.dao.TestDao;
import com.batman.bysj.mongo.domain.TestModel;
import org.assertj.core.util.diff.Chunk;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BatmanBysjCommonMongoApplicationTests {
    @Autowired
    private TestDao testDao;
    Sort sort;
    @Test
    public void contextLoads() {
        List<Map> properties = testDao.getProperties();
        for (Map property : properties) {
            property.forEach((o, o2) -> System.out.print(o + "==>" + o2));
            System.out.println();
        }
    }

    @Test
    public void testGetPropertie(){
        TestModel propertie = testDao.getPropertie("34534534534543");
        System.out.println(propertie.getName());
    }

    @Test
    public void testGetProperties(){
        List<String> strings = Arrays.asList("34534534534543", "123123123132132");
        Map<String, TestModel> propertiesMap = testDao.getPropertiesMap(strings);
        propertiesMap.forEach((s, testModel) -> System.out.println("----" + s));
    }

    @Test
    public void testGetAllPropertiesMap(){
        Map<String, TestModel> allPropertiesMap = testDao.getAllPropertiesMap();
        allPropertiesMap.forEach((s, testModel) -> System.out.println(s));
    }

    @Test
    public void testFindByPage(){
        Page<TestModel> byPage = testDao.findByPage("", new PageRequest(0, 5, sort));
        System.out.println(byPage);
    }

    @Test
    public void testDelete(){
        testDao.deleteAll();
    }

    @Test
    public void testBatchAdd(){
        for(int i = 0;i < 100;i ++){
            TestModel testModel = new TestModel();
            testModel.setName("name" + i);
            testDao.save(testModel);

        }
    }

}

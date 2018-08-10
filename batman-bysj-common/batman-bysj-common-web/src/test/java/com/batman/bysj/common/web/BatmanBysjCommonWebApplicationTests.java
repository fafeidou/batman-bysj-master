package com.batman.bysj.common.web;

import com.batman.bysj.common.model.User;
import com.batman.bysj.common.service.ImageMangerService;
import com.batman.bysj.common.service.UserService;
import com.batman.bysj.mongo.dao.ImageDao;
import com.batman.bysj.mongo.dao.ImageMangerDao;
import com.batman.bysj.mongo.domain.ImageGroup;
import com.batman.bysj.mongo.domain.ImageTestPageRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BatmanBysjCommonWebApplicationTests {
    @Autowired
    ImageMangerService imageMangerService;
    @Autowired
    ImageMangerDao imageMangerDao;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageDao imageDao;

    @Test
    public void contextLoads() {
        ImageTestPageRequest imageTestPageRequest = new ImageTestPageRequest();
        Page<ImageGroup> byPage = imageMangerService.findByPage("", imageTestPageRequest.toPageRequest());
        List<ImageGroup> allActive = imageMangerDao.findAllActive();
        Query query = new Query();
        query.addCriteria(Criteria.where("creator").regex("admin123", "ig"));
        List<ImageGroup> imageGroups = imageMangerDao.find(query);
        System.out.println();
    }

    @Test
    public void test() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletableFuture<List<User>> profitFuture = test1(executorService);
        CompletableFuture<List<User>> profitFuture2 = test2(executorService);
        StopWatch stopWatch = new StopWatch("DB reading...");
        stopWatch.start("111");
        profitFuture.get();
        stopWatch.stop();
        stopWatch.start("222");
        profitFuture2.get();
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());

    }

    @Test
    public void testMo() {
        List<ImageGroup> all = imageDao.findAll();
        ImageGroup imageGroup = new ImageGroup();
        Example<ImageGroup> example = Example.of(imageGroup);
        imageGroup.setDescription("haha");
        example.getMatcher().withIgnoreCase().withIncludeNullValues();
        List<ImageGroup> all1 = imageDao.findAll(example);
        imageMangerDao.testQuery();

    }

    private CompletableFuture<List<User>> test2(ExecutorService executorService) {
        return CompletableFuture.supplyAsync(() -> userService.selectAll(), executorService);
    }

    private CompletableFuture<List<User>> test1(ExecutorService executorService) {
        return CompletableFuture.supplyAsync(() -> userService.selectAll(), executorService);
    }

}

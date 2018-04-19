package com.batman.bysj;

import com.batman.bysj.common.dao.mapper.TestMapper;
import com.batman.bysj.common.model.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@MapperScan(basePackages = "com.batman.bysj.common.dao.mapper")
public class BatmanBysjCommonConfigApplication implements CommandLineRunner {
    @Autowired
    private HelloServiceProperteis  helloServiceProperteis;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    TestMapper testMapper;
    @Override
    public void run(String... strings) throws Exception {
        List<Test> tests = testMapper.selectAll();
        System.out.println("testMapper ------------------------>" + tests.size());
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT  * FROM  test");
        System.out.println(maps.size() + "------------------------------------------------>");
    }

    public static void main(String[] args) {
        SpringApplication.run(BatmanBysjCommonConfigApplication.class, args);
    }
}

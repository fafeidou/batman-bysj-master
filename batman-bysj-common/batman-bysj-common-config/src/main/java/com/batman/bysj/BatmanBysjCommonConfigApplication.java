package com.batman.bysj;

import com.batman.bysj.common.dao.core.mapper.ComCodeMapper;
import com.batman.bysj.common.dao.mapper.TestMapper;
import com.batman.bysj.common.dao.mybatis.MyMapper;
import com.batman.bysj.common.model.ComCode;
import com.batman.bysj.common.model.Test;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.autoconfigure.MapperAutoConfiguration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.core.JdbcTemplate;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

@SpringBootApplication(
        exclude = MapperAutoConfiguration.class
)
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,value = MyMapper.class)})
public class BatmanBysjCommonConfigApplication implements CommandLineRunner {
    @Autowired
    TestMapper testMapper;

    @Autowired
    ComCodeMapper comCodeMapper;

    @Resource(name="coreSqlSessionFactory")
    SqlSessionFactory sqlSessionFactory;
    @Override
    public void run(String... strings) throws Exception {
        List<Test> tests = testMapper.selectAll();
        System.out.println("testMapper ------------------------>" + tests.size());
        List<ComCode> comCodes = comCodeMapper.selectByExampleAndRowBounds(new Example(ComCode.class), new RowBounds(0, 10));
        System.out.println(comCodes.size());
        //方法一
        PageHelper.startPage(1,10);
        List<ComCode> comCodes1 = comCodeMapper.selectAll();
        System.out.println("==============>comCodes1:" + comCodes1.size());
        //方法二
        Page<ComCode> comCodes2 = PageHelper.startPage(1, 10).doSelectPage(() -> comCodeMapper.selectAll());
        System.out.println("==============>comCodes2:" + comCodes2.getResult().size());
        System.out.println("==============>comCodes2.count:" + comCodes2.getTotal());


    }

    public static void main(String[] args) {
        SpringApplication.run(BatmanBysjCommonConfigApplication.class, args);
    }
}

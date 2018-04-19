package com.batman.bysj;

import com.batman.bysj.common.dao.mapper.TestMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.AttributeOverride;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class BatmanBysjCommonBaseApplication{
	public static void main(String[] args) {
		SpringApplication.run(BatmanBysjCommonBaseApplication.class, args);
	}

}

package com.batman.bysj.dao.generator;

import com.batman.bysj.dao.generator.service.GeneratorService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileOutputStream;

@SpringBootApplication
public class BatmanBysjCommonDaoGeneratorApplication implements CommandLineRunner {
    @Autowired
    private GeneratorService generatorService;

    public static void main(String[] args) {
        SpringApplication.run(BatmanBysjCommonDaoGeneratorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        byte[] com_codes = generatorService.generatorCode(new String[]{"com_code"});
        IOUtils.write(com_codes, new FileOutputStream(new File("D:\\test\\test.zip")));
    }
}

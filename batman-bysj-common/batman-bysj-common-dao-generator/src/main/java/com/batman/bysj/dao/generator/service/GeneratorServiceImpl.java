package com.batman.bysj.dao.generator.service;

import com.batman.bysj.dao.generator.jdbc.GeneratorMapper;
import com.batman.bysj.dao.generator.utils.GenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @author victor.qin
 * @date 2018/5/24 17:07
 */
@Service
public class GeneratorServiceImpl implements GeneratorService {
    @Autowired
    private GeneratorMapper generatorMapper;

    @Override
    public byte[] generatorCode(String[] tableNames) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
//        FileOutputStream zip = new FileOutputStream("D:\\test\\");
        for (String tableName : tableNames) {
            //查询表信息
            Map<String, String> table = generatorMapper.get(tableName);
            //查询列信息
            List<Map<String, Object>> columns = generatorMapper.listColumns(tableName);
            //生成代码
            GenUtils.generatorCode(table, columns, zip);
        }
        zip.close();
        return outputStream.toByteArray();
    }
}

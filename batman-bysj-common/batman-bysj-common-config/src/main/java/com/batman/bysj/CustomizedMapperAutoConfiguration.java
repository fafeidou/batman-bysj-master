package com.batman.bysj;

import com.github.pagehelper.autoconfigure.MapperProperties;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

import javax.annotation.PostConstruct;
import java.util.List;

import tk.mybatis.mapper.common.Mapper;

/**
 * @author victor.qin
 * @date 2018/5/24 13:28
 */
@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
@EnableConfigurationProperties(MapperProperties.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class CustomizedMapperAutoConfiguration {

    private final List<SqlSessionFactory> sqlSessionFactoryList;
    @Autowired
    private MapperProperties properties;

    public CustomizedMapperAutoConfiguration(List<SqlSessionFactory> sqlSessionFactoryList) {
        this.sqlSessionFactoryList = sqlSessionFactoryList;
    }

    @PostConstruct
    public void addPageInterceptor() {
        MapperHelper mapperHelper = new MapperHelper();
        mapperHelper.setConfig(properties);
        if (properties.getMappers().size() > 0) {
            for (Class mapper : properties.getMappers()) {
                mapperHelper.registerMapper(mapper);
            }
        } else {
            mapperHelper.registerMapper(Mapper.class);
        }
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            mapperHelper.processConfiguration(sqlSessionFactory.getConfiguration());
        }
    }

}

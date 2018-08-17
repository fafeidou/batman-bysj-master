package com.batman.bysj;

import com.alibaba.druid.pool.DruidDataSource;
import com.batman.bysj.common.dao.mapper.TestMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author victor.qin
 * @date 2018/4/18 10:28
 */
@Configuration
@MapperScan(/*basePackages = "com.batman.bysj.common.dao.mapper",*/
        sqlSessionTemplateRef = "bysjSqlSessionTemplate", basePackageClasses = TestMapper.class)
@EnableConfigurationProperties(value = BysjDruidDBProperties.class)
public class BysjDruidDBAutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(BysjDruidDBAutoConfiguration.class);

    private final BysjDruidDBProperties bysjDruidDBProperties;

    @Autowired
    public BysjDruidDBAutoConfiguration(BysjDruidDBProperties bysjDruidDBProperties) {
        this.bysjDruidDBProperties = bysjDruidDBProperties;
    }

    @Bean(name = "bysjDataSource")
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    public DataSource bysjDataSource() {
        DruidDataSource datasource = new DruidDataSource();

        datasource.setUrl(bysjDruidDBProperties.getUrl());
        datasource.setUsername(bysjDruidDBProperties.getUsername());
        datasource.setPassword(bysjDruidDBProperties.getPassword());
        datasource.setDriverClassName(bysjDruidDBProperties.getDriverClassName());

        //configuration
        datasource.setInitialSize(bysjDruidDBProperties.getInitialSize());
        datasource.setMinIdle(bysjDruidDBProperties.getMinIdle());
        datasource.setMaxActive(bysjDruidDBProperties.getMaxActive());
        datasource.setMaxWait(bysjDruidDBProperties.getMaxWait() == 0 ? bysjDruidDBProperties.getMaxWait() : 6000);
        datasource.setTimeBetweenEvictionRunsMillis(bysjDruidDBProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(bysjDruidDBProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(bysjDruidDBProperties.getValidationQuery());
        datasource.setTestWhileIdle(bysjDruidDBProperties.isTestWhileIdle());
        datasource.setTestOnBorrow(bysjDruidDBProperties.isTestOnBorrow());
        datasource.setTestOnReturn(bysjDruidDBProperties.isTestOnReturn());
        datasource.setPoolPreparedStatements(bysjDruidDBProperties.isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(bysjDruidDBProperties.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            datasource.setFilters(bysjDruidDBProperties.getFilters());
        } catch (SQLException e) {
            logger.error("druid configuration initialization filter", e);
        }
        datasource.setConnectionProperties(bysjDruidDBProperties.getConnectionProperties());

        return datasource;
    }

    @Bean(name = "bysjSqlSessionFactory")
    @Primary
    public SqlSessionFactory bysjSqlSessionFactory(@Qualifier("bysjDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(
                "classpath:mapper/bysj/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "bysjTransactionManager")
    @Primary
    public DataSourceTransactionManager bysjTransactionManager(@Qualifier("bysjDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    @Bean(name = "bysjSqlSessionTemplate")

    @Primary
    public SqlSessionTemplate bysjSqlSessionTemplate(@Qualifier("bysjSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}



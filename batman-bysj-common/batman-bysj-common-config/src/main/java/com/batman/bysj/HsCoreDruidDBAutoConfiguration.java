package com.batman.bysj;

import com.alibaba.druid.pool.DruidDataSource;
import com.batman.bysj.common.dao.core.mapper.ComCodeMapper;
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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author victor.qin
 * @date 2018/4/18 10:28
 */
@Configuration
@MapperScan(/*basePackages = "com.batman.bysj.common.dao.core.mapper", */sqlSessionTemplateRef = "coreSqlSessionTemplate", basePackageClasses = ComCodeMapper.class)
@EnableConfigurationProperties(value = HsCoreDruidDBProperties.class)
public class HsCoreDruidDBAutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(HsCoreDruidDBAutoConfiguration.class);

    private final HsCoreDruidDBProperties bysjDruidDBProperties;

    @Autowired
    public HsCoreDruidDBAutoConfiguration(HsCoreDruidDBProperties bysjDruidDBProperties) {
        this.bysjDruidDBProperties = bysjDruidDBProperties;
    }

    @Bean(name = "coreDataSource")
//    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    public DataSource coreDataSource() {
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

    @Bean(name = "coreSqlSessionFactory")
    public SqlSessionFactory coreSqlSessionFactory(@Qualifier("coreDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(
                "classpath:mapper/core/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "coreTransactionManager")
    public DataSourceTransactionManager coreTransactionManager(@Qualifier("coreDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "coreSqlSessionTemplate")
    public SqlSessionTemplate coreSqlSessionTemplate(@Qualifier("coreSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

//    @Bean
//    public ServletRegistrationBean druidServlet() {
//        ServletRegistrationBean reg = new ServletRegistrationBean();
////        reg.setServlet(new StatViewServlet());
//        reg.addUrlMappings("/druid/*");
//        reg.addInitParameter("allow", ""); //白名单
//        return reg;
//    }
//
//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
////        filterRegistrationBean.setFilter(new WebStatFilter());
//        filterRegistrationBean.addUrlPatterns("/*");
//        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
//        filterRegistrationBean.addInitParameter("profileEnable", "true");
//        filterRegistrationBean.addInitParameter("principalCookieName","USER_COOKIE");
//        filterRegistrationBean.addInitParameter("principalSessionName","USER_SESSION");
//        filterRegistrationBean.addInitParameter("DruidWebStatFilter","/*");
//        return filterRegistrationBean;
//    }
}



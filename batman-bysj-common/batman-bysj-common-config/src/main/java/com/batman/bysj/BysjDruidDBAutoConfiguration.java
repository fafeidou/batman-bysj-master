package com.batman.bysj;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author victor.qin
 * @date 2018/4/18 10:28
 */
@Configuration
@EnableConfigurationProperties(value = BysjDruidDBProperties.class)
public class BysjDruidDBAutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(BysjDruidDBAutoConfiguration.class);

    private final  BysjDruidDBProperties bysjDruidDBProperties;

    @Autowired
    public BysjDruidDBAutoConfiguration(BysjDruidDBProperties bysjDruidDBProperties) {
        this.bysjDruidDBProperties = bysjDruidDBProperties;
    }

    @Bean
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();

        datasource.setUrl(bysjDruidDBProperties.getUrl());
        datasource.setUsername(bysjDruidDBProperties.getUsername());
        datasource.setPassword(bysjDruidDBProperties.getPassword());
        datasource.setDriverClassName(bysjDruidDBProperties.getDriverClassName());

        //configuration
        datasource.setInitialSize(bysjDruidDBProperties.getInitialSize());
        datasource.setMinIdle(bysjDruidDBProperties.getMinIdle());
        datasource.setMaxActive(bysjDruidDBProperties.getMaxActive());
        datasource.setMaxWait(bysjDruidDBProperties.getMaxWait());
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



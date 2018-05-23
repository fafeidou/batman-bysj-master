package com.batman.bysj;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author victor.qin
 * @date 2018/5/18 14:41
 */
public class Test implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet------------->");
    }
}

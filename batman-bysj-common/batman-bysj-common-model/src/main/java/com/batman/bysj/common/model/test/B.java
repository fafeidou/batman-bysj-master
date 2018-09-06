package com.batman.bysj.common.model.test;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author victor.qin
 * @date 2018/9/5 15:45
 */
public class B extends A {
    private BigDecimal high;

    @Override
    public String toString() {
        return "B{" +
                "high=" + high +
                ", age='" + age + '\'' +
                ", name='" + name + '\'' +
                ", score=" + score +
                '}';
    }

    @Override
    public void setDefaultZero(List<String> fields) {
        super.setDefaultZero(fields);
    }
}

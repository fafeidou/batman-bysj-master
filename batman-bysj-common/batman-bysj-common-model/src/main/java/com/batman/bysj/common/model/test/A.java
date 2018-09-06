package com.batman.bysj.common.model.test;

import lombok.Data;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author victor.qin
 * @date 2018/9/5 15:44
 */
@Data
public class A {
    protected String age;
    protected String name;
    protected BigDecimal score;


    public void setDefaultZero(List<String> fields) {
        Class clz = this.getClass();
        for (; clz != Object.class; clz = clz.getSuperclass()) {
            if(clz.equals(A.class)) {
                Field[] clsFields = clz.getDeclaredFields();
                for (Field field : clsFields) {
                    try {
                        if (fields.contains(field.getName())
                                && null == field.get(this)
                                && field.getType().equals(BigDecimal.class)) {
                            field.set(this, BigDecimal.ZERO);
                        }
                        if (fields.contains(field.getName())
                                && null == field.get(this)
                                && field.getType().equals(String.class)) {
                            field.set(this, "RRR");
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

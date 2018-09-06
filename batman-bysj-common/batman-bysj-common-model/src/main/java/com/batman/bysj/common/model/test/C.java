package com.batman.bysj.common.model.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author victor.qin
 * @date 2018/9/5 15:46
 */
public class C {
    public static void main(String[] args) {


        List<String> fields = Arrays.asList("age",
                "name",
                "score", "high");
        B b = new B();
        b.setName("1111");
        b.setDefaultZero(fields);
        System.out.println(b);

        List<String> list = new ArrayList<>();
        list.add(null);
        System.out.println(list.stream().anyMatch(java.util.Objects::nonNull));

    }
}

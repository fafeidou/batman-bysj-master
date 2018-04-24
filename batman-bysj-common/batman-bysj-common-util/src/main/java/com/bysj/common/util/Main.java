package com.bysj.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author victor.qin
 * @date 2018/4/20 14:56
 */
public class Main {
    private String str;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public static void main(String[] args) {
        String ss = "";

        String collect = Optional.ofNullable(Arrays.asList(ss).stream().collect(Collectors
                .joining(","))).orElse(StringUtils.EMPTY);

        Main main = new Main();
        main.setStr(collect);
        System.out.println(main.getStr());
        System.out.println("-----------------"  + collect);
    }
}

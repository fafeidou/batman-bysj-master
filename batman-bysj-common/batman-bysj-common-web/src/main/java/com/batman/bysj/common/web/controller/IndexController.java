package com.batman.bysj.common.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author victor.qin
 * @date 2018/6/5 9:38
 */
@Controller
public class IndexController {
    @RequestMapping("/hello")
    public String hello() {
        return "model/hello";
    }

    @RequestMapping("/helloVue")
    public String helloVue() {
        return "model/helloVue";
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/main.html")
    public String main() {
        return "main";
    }
}

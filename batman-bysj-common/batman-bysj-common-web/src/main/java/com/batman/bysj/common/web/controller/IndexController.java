package com.batman.bysj.common.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author victor.qin
 * @date 2018/6/5 9:38
 */
//@Api(tags = "测试swagger")
@Controller
public class IndexController {

    //    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/hello")
//    @ApiOperation(value = "查询测试", notes = "查询测试1")
//    @ApiImplicitParam(name = "product", value = "产品信息", required = true, dataType = "Product")
    public String hello() {
        return "model/hello";
    }

    @RequestMapping("/helloVue")
    public String helloVue() {
        return "model/helloVue";
    }

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/main.html")
    public String main(){
        return "main";
    }
}

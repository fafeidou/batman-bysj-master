package com.batman.bysj.common.web.controller;

import com.batman.bysj.common.web.domin.Test;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.management.MonitorInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * @author victor.qin
 * @date 2018/6/5 9:38
 */
@Api(tags ="测试swagger")
@Controller
public class TestController {

    @RequestMapping("/hello")
    @ApiOperation(value = "查询测试", notes = "查询测试1")
    @ApiImplicitParam(name = "product", value = "产品信息", required = true, dataType = "Product")
    public String hello(ModelMap modelMap){
        modelMap.put("hello","hello");
        return "model/hello";
    }

    @RequestMapping("/helloVue")
    public String helloVue(ModelMap modelMap){
        modelMap.put("hello","hello");
        return "model/helloVue";
    }

    @ResponseBody
    @RequestMapping("/getList")
    public List<Test> getList(){
        List<Test> list = new ArrayList<>();
        Test test = new Test();
        test.setName("123123");
        Test test2 = new Test();
        test2.setName("345345");
        list.add(test);
        list.add(test2);
        return list;
    }

}

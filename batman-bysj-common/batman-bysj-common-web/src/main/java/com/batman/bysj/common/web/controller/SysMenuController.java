package com.batman.bysj.common.web.controller;

import com.batman.bysj.common.model.SysMenu;
import com.batman.bysj.common.service.SysMenuService;
import com.batman.bysj.common.web.domin.WebApiResponse;
import javassist.tools.web.Webserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author victor.qin
 * @date 2018/8/14 18:04
 */
@Controller
@RequestMapping("/sys/menu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;
    @RequestMapping("/nav")
    @ResponseBody
    public WebApiResponse<List<SysMenu>> nav(){
        return WebApiResponse.success(sysMenuService.getAllMenuList());
    }
}

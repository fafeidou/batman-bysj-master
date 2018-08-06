package com.batman.bysj.common.web.controller;

import com.batman.bysj.common.web.constants.UrlConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author victor.qin
 * @date 2018/8/1 10:32
 */
@Controller
public class LoginController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = UrlConstants.UserUrl.LOGIN)
    public String login() {
        return "login";
    }
}

package com.batman.bysj.common.web.controller;

import com.batman.bysj.common.model.User;
import com.batman.bysj.common.model.request.UserForm;
import com.batman.bysj.common.model.response.UserPageBean;
import com.batman.bysj.common.service.UserService;
import com.batman.bysj.common.web.constants.UrlConstants;
import com.batman.bysj.common.web.domin.WebApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.ListUtils;

import java.util.Arrays;

/**
 * @author victor.qin
 * @date 2018/6/23 14:58
 */
@Controller
@RequestMapping(UrlConstants.UserUrl.ROOT)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = UrlConstants.UserUrl.GET_USER_PAPE)
    @ResponseBody
    public WebApiResponse<UserPageBean> getUserPage(@RequestBody UserForm userForm) {
        return WebApiResponse.success(userService.getUserPage(userForm));
    }

    @PostMapping(value = UrlConstants.UserUrl.SAVE)
    @ResponseBody
    public WebApiResponse<Integer> save(@RequestBody User user) {
        return WebApiResponse.success(userService.save(user));
    }

    @PostMapping(value = UrlConstants.UserUrl.UPDATE)
    @ResponseBody
    public WebApiResponse<Integer> update(@RequestBody User user) {
        return WebApiResponse.success(userService.update(user));
    }

    @PostMapping(value = UrlConstants.UserUrl.DELETE)
    @ResponseBody
    public WebApiResponse<Integer> delete(@RequestBody Integer[] ids) {
        if (!ListUtils.isEmpty(Arrays.asList(ids))) {
            for (Integer integer : Arrays.asList(ids)) {
                userService.deleteByKey(integer);
            }
        }
        return WebApiResponse.success(0);
    }

    @GetMapping(value = UrlConstants.UserUrl.INFO)
    @ResponseBody
    public WebApiResponse<User> info(@RequestParam("id") Integer id) {
        return WebApiResponse.success(userService.selectByKey(id));
    }

    @GetMapping(value = "aaa")
    @ResponseBody
    public UserForm info() {
        return new UserForm();
    }
}

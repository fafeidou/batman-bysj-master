package com.batman.bysj.common.service;

import com.batman.bysj.common.model.User;
import com.batman.bysj.common.model.request.UserForm;
import com.batman.bysj.common.model.response.UserPageBean;

/**
 * @author victor.qin
 * @date 2018/6/23 14:56
 */
public interface UserService extends IService<User> {
    UserPageBean getUserPage(UserForm userForm);
}

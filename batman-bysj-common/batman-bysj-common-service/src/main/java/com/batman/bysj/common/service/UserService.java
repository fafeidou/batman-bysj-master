package com.batman.bysj.common.service;

import com.batman.bysj.common.model.UserInfoX;
import com.batman.bysj.common.model.request.UserForm;
import com.batman.bysj.common.model.response.UserPageBean;

/**
 * @author victor.qin
 * @date 2018/6/23 14:56
 */
public interface UserService extends IService<UserInfoX> {
    UserPageBean getUserPage(UserForm userForm);

    UserInfoX getUserByName(String userName);
}

package com.batman.bysj.common.service.impl;

import com.batman.bysj.common.model.User;
import com.batman.bysj.common.model.request.UserForm;
import com.batman.bysj.common.model.response.UserPageBean;
import com.batman.bysj.common.service.BaseService;
import com.batman.bysj.common.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @author victor.qin
 * @date 2018/6/23 14:56
 */
@Service
public class UserServiceImpl extends BaseService<User> implements UserService {

    @Override
    public UserPageBean getUserPage(UserForm userForm) {
        UserPageBean userPageBean = new UserPageBean();
        User user = new User();
        BeanUtils.copyProperties(userForm, user);
        Page<User> userList = PageHelper.startPage(
                userForm).doSelectPage(() -> this.select(user));
        userPageBean.setRecord(userList);
        userPageBean.setTotal(userList.getTotal());
        userPageBean.setTotalPage(userList.getPages());
        return userPageBean;
    }
}

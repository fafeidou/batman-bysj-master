package com.batman.bysj.common.service.impl;

import com.batman.bysj.common.dao.mapper.UserMapper;
import com.batman.bysj.common.model.UserInfoX;
import com.batman.bysj.common.model.request.UserForm;
import com.batman.bysj.common.model.response.UserPageBean;
import com.batman.bysj.common.service.BaseService;
import com.batman.bysj.common.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author victor.qin
 * @date 2018/6/23 14:56
 */
@Service
public class UserServiceImpl extends BaseService<UserInfoX> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserPageBean getUserPage(UserForm userForm) {
        UserPageBean userPageBean = new UserPageBean();
        UserInfoX user = new UserInfoX();
        BeanUtils.copyProperties(userForm, user);
        Page<UserInfoX> userList = PageHelper.startPage(
                userForm).doSelectPage(() -> this.select(user));
        userPageBean.setRecord(userList);
        userPageBean.setTotal(userList.getTotal());
        userPageBean.setTotalPage(userList.getPages());
        return userPageBean;
    }

    @Override
    public UserInfoX getUserByName(String userName) {
        return userMapper.selectUserByName(userName);
    }
}

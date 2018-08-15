package com.batman.bysj.common.service;


import com.batman.bysj.common.model.UserRole;

import java.util.List;

/**
 * @author victor.qin
 * @date 2018/6/23 14:56
 */
public interface UserRoleService extends IService<UserRole> {
    List<UserRole> getUserRolesByUserId(Integer id);
}

package com.batman.bysj.common.service;

import com.batman.bysj.common.model.SysMenu;

import java.util.List;

/**
 * @author victor.qin
 * @date 2018/6/23 14:56
 */
public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> getAllMenuList();
}

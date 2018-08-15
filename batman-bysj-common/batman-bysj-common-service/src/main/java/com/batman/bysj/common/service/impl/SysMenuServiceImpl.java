package com.batman.bysj.common.service.impl;

import com.batman.bysj.common.model.SysMenu;
import com.batman.bysj.common.service.BaseService;
import com.batman.bysj.common.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author victor.qin
 * @date 2018/8/14 17:54
 */
@Service
public class SysMenuServiceImpl extends BaseService<SysMenu> implements SysMenuService {
    @Override
    public List<SysMenu> getAllMenuList() {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setParentId((long) 1);
        return select(sysMenu);
        //递归获取子菜单
//        getMenuTreeList(null, menuIdList);

//        return menuList;
    }

    /**
     * 递归
     */
//    private List<SysMenu> getMenuTreeList(List<SysMenu> menuList, List<Long> menuIdList){
//        List<SysMenu> subMenuList = new ArrayList<SysMenu>();
//
//        for(SysMenu entity : menuList){
//            //目录
//            if(entity.getType() == Constant.MenuType.CATALOG.getValue()){
//                entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
//            }
//            subMenuList.add(entity);
//        }
//
//        return subMenuList;
//    }
}

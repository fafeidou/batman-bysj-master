package com.batman.bysj.common.web.service;

import com.batman.bysj.common.model.UserInfoX;
import com.batman.bysj.common.service.UserService;
import com.batman.bysj.common.web.domin.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author victor.qin
 * @date 2018/8/15 21:03
 */
@Service
public class UserVoService {
    @Autowired
    UserService userService;

    public UserVo getUserByName(String userName) {
        UserInfoX userByName = userService.getUserByName(userName);
        if (userByName != null) {
            UserVo userVo = new UserVo();
            userVo.setUserName(userByName.getUserName());
            userVo.setPassword(userByName.getPassword());
            userVo.setId(userByName.getId());
            return userVo;
        }
        return null;
    }
}

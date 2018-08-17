package com.batman.bysj.common.web.service;

import com.batman.bysj.common.model.UserRole;
import com.batman.bysj.common.service.RoleService;
import com.batman.bysj.common.service.UserRoleService;
import com.batman.bysj.common.web.domin.vo.UserVo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author victor.qin
 * @date 2018/8/11 16:40
 */
public class LightSwordUserDetailService implements UserDetailsService {
    @Autowired
    private UserVoService userVoService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        userName = "admin1";
        UserVo userVo = userVoService.getUserByName(userName);
        if (userVo == null) {
            throw new UsernameNotFoundException("userName:" + userName + " not found");
        }
        List<SimpleGrantedAuthority> authorities = Lists.newArrayList();
        List<UserRole> userRoles = userRoleService.getUserRolesByUserId(userVo.getId());
        List<String> roles = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userRoles)) {
            userRoles.forEach(userRole -> {
                String roleName = Optional.of(roleService.selectByKey(userRole.getId()).getRole()).orElse(org.apache.commons.lang3.StringUtils.EMPTY);
                roles.add(roleName);
                authorities.add(new SimpleGrantedAuthority(roleName));
            });
        }

        return new User(userVo.getUsername(), userVo.getPassword(), authorities);
    }
}

package com.batman.bysj.common.web.service;

/**
 * @author victor.qin
 * @date 2018/8/11 16:40
 */
//public class LightSwordUserDetailService implements UserDetailsService {
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private RoleService roleService;
//    @Autowired
//    private UserRoleService userRoleService;
//
//    @Override
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        userName = "admin1";
////        User user = userService.getUserByName(userName);
//        User user = new User();
//        user.setUserName("admin1");
//        user.setPassword("");
//        if (user == null) {
//            throw new UsernameNotFoundException("userName:" + userName + " not found");
//        }
//        List<SimpleGrantedAuthority> authorities = Lists.newArrayList();
//        List<UserRole> userRoles = userRoleService.getUserRolesByUserId(user.getId());
//        if (!CollectionUtils.isEmpty(userRoles)) {
//            userRoles.forEach(userRole -> {
//                String roleName = Optional.of(roleService.selectByKey(userRole.getId()).getRole()).orElse(org.apache.commons.lang3.StringUtils.EMPTY);
//                authorities.add(new SimpleGrantedAuthority(roleName));
//            });
//        }
//        return new UserVo(userName, user.getPassword(), authorities);
//    }
//}

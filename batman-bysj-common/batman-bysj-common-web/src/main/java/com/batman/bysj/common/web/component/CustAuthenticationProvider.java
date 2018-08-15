package com.batman.bysj.common.web.component;

/**
 * @author victor.qin
 * @date 2018/8/14 13:06
 */
//public class CustAuthenticationProvider implements AuthenticationProvider {
//
//    @Autowired
//    private LightSwordUserDetailService userService;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getName();
//        String password = (String) authentication.getCredentials();
//        UserDetails userDetails = userService.loadUserByUsername(username);
//        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
//        return new UsernamePasswordAuthenticationToken(userDetails, password, authorities);
//    }
//
//    @Override
//    public boolean supports(Class<?> arg0) {
//        return true;
//    }
//}

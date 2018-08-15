package com.batman.bysj.common.web.config;

/**
 * @author victor.qin
 * @date 2018/8/1 10:23
 */
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    @Bean
//    public UserDetailsService userDetailsService() { //覆盖写userDetailsService方法 (1)
//        return new LightSwordUserDetailService();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//        http.authorizeRequests()
//                .antMatchers("/").permitAll()
//                .antMatchers("/css/**",
//                        "/fonts/**",
//                        "/js/**",
//                        "/libs/**",
//                        "/plugins/**"
//                ).permitAll()
//                .anyRequest().authenticated().and()
//                .formLogin().loginPage("/login")
//                .defaultSuccessUrl("/hello").permitAll().and()
//                .logout().permitAll();
//
//        http.logout().logoutSuccessUrl("/");
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService());
//    }
//
//}

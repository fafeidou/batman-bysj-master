package com.batman.bysj.common.web.filter;

/**
 * @author victor.qin
 * @date 2018/8/11 15:43
 */

//@Order(1)
//@WebFilter(filterName = "loginFilter", urlPatterns = "/*")
//public class LoginFilter implements Filter {
//    @Override
//    public void init(FilterConfig filterConfig) {
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpSession session = httpRequest.getSession();
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String userName = "";
//        if (principal instanceof UserDetails) {
//            userName = ((UserDetails) principal).getUsername();
//        } else {
//            userName = principal.toString();
//        }
//        session.setAttribute("userName", userName);
//        chain.doFilter(request, response);
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}

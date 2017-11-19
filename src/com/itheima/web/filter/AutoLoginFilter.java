package com.itheima.web.filter;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itheima.domain.User;
import com.itheima.service.UserService;

public class AutoLoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        
        //  获得cookies中的用户名和密码
        String cookie_username = null;
        String cookie_password = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("cookie_username".equals(cookie.getName())) {
                    cookie_username = cookie.getValue();
                }
                if ("cookie_password".equals(cookie.getName())) {
                    cookie_password = cookie.getValue();
                }
            }
        }
        //  判断用户名密码是否是null
        if (cookie_username != null && cookie_password != null) {
            UserService service = new UserService();
            User user = null;
            try {
                user = service.login(cookie_username, cookie_password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            session.setAttribute("user", user);
        }
        chain.doFilter(req, res);
        
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }

}

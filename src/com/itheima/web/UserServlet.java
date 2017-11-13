package com.itheima.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.itheima.domain.User;
import com.itheima.service.UserService;
import com.itheima.utils.CommonsUtils;
import com.itheima.utils.MD5Utils;
import com.itheima.utils.MailUtils;
import com.sun.org.apache.bcel.internal.generic.ISHL;

public class UserServlet extends BaseServlet {

    // 激活用户功能
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String activeCode = request.getParameter("activeCode");
        System.out.println("activeCode1:" + activeCode);
        UserService service = new UserService();
        try {
            service.active(activeCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 校验用户名是否存在的功能
    public void checkUsername(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        System.out.println(username);
        UserService service = new UserService();
        boolean isExist = false;
        try {
            isExist = service.checkUsername(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String json = "{\"isExist\":" + isExist + "}";
        response.getWriter().write(json);
    }

    // 注册功能
    public void register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // 获得表单数据
        Map<String, String[]> properties = request.getParameterMap();
        User user = new User();
        try {
            // 自己指定一个类型转换器（将String转成Date）
            ConvertUtils.register(new Converter() {
                @Override
                public Object convert(Class clazz, Object value) {
                    // 将string转成date
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date parse = null;
                    try {
                        parse = format.parse(value.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return parse;
                }
            }, Date.class);
            // 映射封装
            BeanUtils.populate(user, properties);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        // private String uid;
        user.setUid(CommonsUtils.getUUID());
        // private String telephone;
        user.setTelephone(null);
        // private int state;//是否激活
        user.setState(0);
        // private String code;//激活码
        String activeCode = CommonsUtils.getUUID();
        user.setCode(activeCode);

        // 将user传递给service层
        UserService service = new UserService();
        boolean isRegisterSuccess = service.regist(user);
        System.out.println(isRegisterSuccess);

        if (isRegisterSuccess) {
            String emailMsg = "您好，请点击该链接激活您的账户：" + "<a href=\"http://localhost:8080/SHOP/user?method=active&activeCode="
                    + activeCode + "\">http://localhost:8080/SHOP/user?method=active&activeCode=" + activeCode + "</a>";
            try {
                MailUtils.sendMail(user.getEmail(), emailMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }

            response.sendRedirect(request.getContextPath() + "/registerSuccess.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/registerFail.jsp");
        }
    }

    // 用户登录
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        //  获取用户的用户名和密码
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //  对密码进行加密
        password = MD5Utils.md5(password);
        //判断用户是否存在
        UserService service = new UserService();
        User user = null;
        try {
            user = service.login(username, username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (user != null) {
            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else {
            request.setAttribute("loginError", "用户名或密码错误");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

}
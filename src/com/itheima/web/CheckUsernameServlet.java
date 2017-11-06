package com.itheima.web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.service.UserService;

public class CheckUsernameServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
package com.itheima.web;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
            //  1.获得请求的method名称
            String methodName = request.getParameter("method");
            //  2.获得当前访问的对象的字节码对象
            Class clazz = this.getClass();
            //  3.获得当前字节码对象中指定的方法
            Method method = clazz.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //  4.执行该方法
            method.invoke(this, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
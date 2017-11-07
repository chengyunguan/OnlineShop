package com.itheima.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.itheima.domain.Category;
import com.itheima.service.CategoryListService;

public class CategoryListServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CategoryListService service = new CategoryListService();
        List<Category> categoryList = service.findCategoryList();
        response.setContentType("text/html;charset=UTF-8");
        Gson gson = new Gson();
        //  封装categoryList为json格式
        String json = gson.toJson(categoryList); 
        response.getWriter().write(json);
        System.out.println(json);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
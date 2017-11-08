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
import com.itheima.utils.JedisPoolUtils;

import redis.clients.jedis.Jedis;

public class CategoryListServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        //  使用redis优化。当categoryList存在与redis中时，直接取出
        Jedis jedis = JedisPoolUtils.getJedis();
        String categoryListStr = jedis.get("categoryList");
        List<Category> categoryList = null;
        
        //  若redis中不存在categoryList，则从数据库中取出，并存取redis以待下一次使用
        if (categoryList  == null) {
            CategoryListService service = new CategoryListService();
            categoryList = service.findCategoryList();
        //  封装categoryList为json格式
            Gson gson = new Gson();
            categoryListStr = gson.toJson(categoryList); 
        }
        
        //  将categoryListStr写入response中
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(categoryListStr);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
package com.itheima.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.domain.Product;
import com.itheima.service.ProductService;

public class IndexServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //  展示热门商品
        ProductService service = new ProductService();
        List<Product> hotProductList = service.findHotProductList();
        request.setAttribute("hotProductList", hotProductList);
        
        //  展示最新商品
        List<Product> newProductList = service.findNewProductList();
        request.setAttribute("newProductList", newProductList);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
//        response.sendRedirect("/index.jsp");
        
        //  直接在控制台输出从数据库中读取的商品数据
        System.out.println("newProductList" + newProductList);
        System.out.println("hotProductList" + hotProductList);
        
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
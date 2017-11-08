package com.itheima.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.domain.Product;
import com.itheima.service.ProductService;

public class ProductInfoServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //  获得商品pid
        String pid = request.getParameter("pid");
        //  从数据库根据pid获得商品信息
        ProductService service = new ProductService();
        Product product = service.getProductInfo(pid);
        //  将商品信息放入request域中
        System.out.println(product.toString());
        request.setAttribute("product", product);
        request.getRequestDispatcher("/product_info.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
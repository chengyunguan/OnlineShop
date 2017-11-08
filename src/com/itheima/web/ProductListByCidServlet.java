package com.itheima.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.domain.PageBean;
import com.itheima.domain.Product;
import com.itheima.service.ProductService;

public class ProductListByCidServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //  获得cid
        String cid = request.getParameter("cid");
        //  获得currentPage       
        String currentPageStr = request.getParameter("currentPage"); 
        if (currentPageStr == null) {
            currentPageStr = "1";
        }
        int currentPage = Integer.parseInt(currentPageStr);
        //  获得currentCount
        int currentCount = 12;
        //  通过cid查询商品
        ProductService service = new ProductService();
        PageBean<Product> pageBean = service.findPoductListByCid(cid, currentPage, currentCount);
        //  将pageBean放到request域
        request.setAttribute("pageBean", pageBean);
        //  转发到product_list.jsp页面        
        request.setAttribute("cid", cid);
        request.getRequestDispatcher("/product_list.jsp").forward(request, response);
        
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
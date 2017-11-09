package com.itheima.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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
//        System.out.println("currentPageStr:" + currentPage);
        //  获得currentCount
        int currentCount = 12;
        //  通过cid查询商品
        ProductService service = new ProductService();
        PageBean<Product> pageBean = service.findPoductListByCid(cid, currentPage, currentCount);
       
        
        //  定义一个记录历史商品信息的集合
        List<Product> historyProductList = new ArrayList<Product>();
        //  获取客户端的名为pids的cookie
        String pids = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("pids")) {
                    pids = cookie.getValue();
                    //  拆分pids串
                    String[] split = pids.split("-");
                    for (String pid : split) {
                        Product product = service.getProductInfoByPid(pid);
                        historyProductList.add(product);
                    }
                }
            }
        }
        
        //  将pageBean放到request域
        request.setAttribute("pageBean", pageBean);
        //  转发到product_list.jsp页面        
        request.setAttribute("cid", cid);
        //  将历史记录的集合放到request域中
        request.setAttribute("historyProductList", historyProductList);
        request.getRequestDispatcher("/product_list.jsp").forward(request, response);
        
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
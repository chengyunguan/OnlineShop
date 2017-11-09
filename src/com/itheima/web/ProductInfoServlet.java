package com.itheima.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.domain.Product;
import com.itheima.service.ProductService;

public class ProductInfoServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //  获得当前页
        String currentPage = request.getParameter("currentPage");
        //  获得类别id
        String cid = request.getParameter("cid");
        //  获得商品pid
        String pid = request.getParameter("pid");
        //  从数据库根据pid获得商品信息
        ProductService service = new ProductService();
        Product product = service.getProductInfoByPid(pid);
        //  将商品信息放入request域中
        request.setAttribute("product", product);
        request.setAttribute("cid", cid);
        request.setAttribute("currentPage", currentPage);
//        System.out.println("cid:" + cid);
//        System.out.println("currentPage:" + currentPage);
        
        //  获得客户端携带的名字叫pids的cookie
        String pids = pid;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("pids".equals(cookie.getName())) {
                    pids = cookie.getValue();
                    //  将split拆成一个数组，再封装成链表
                    String[] split = pids.split("-");
                    List<String> asList = Arrays.asList(split);
                    LinkedList<String> list = new LinkedList<String>(asList);
                    //  判断cookie是否包含当前商品的pid
                    if (list.contains(pid)) {
                        list.remove(pid);
                    }
                    list.addFirst(pid);
                    StringBuffer sb = new StringBuffer();
                    for(int i = 0; i < list.size() && i < 7; i++) {
                        sb.append(list.get(i));
                        sb.append('-');
                    }
                    pids = sb.substring(0, sb.length() - 1);
                    System.out.println("pids:" + pids);
                }
            }
        }
        Cookie cookie = new Cookie("pids", pids);
        response.addCookie(cookie);
        
        
        request.getRequestDispatcher("/product_info.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
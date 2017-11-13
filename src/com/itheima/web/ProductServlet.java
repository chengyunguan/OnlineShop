package com.itheima.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.itheima.domain.Cart;
import com.itheima.domain.CartItem;
import com.itheima.domain.Category;
import com.itheima.domain.OrderItem;
import com.itheima.domain.Orders;
import com.itheima.domain.PageBean;
import com.itheima.domain.Product;
import com.itheima.domain.User;
import com.itheima.service.CategoryListService;
import com.itheima.service.ProductService;
import com.itheima.utils.CommonsUtils;
import com.itheima.utils.JedisPoolUtils;

import redis.clients.jedis.Jedis;

public class ProductServlet extends BaseServlet {

    //  显示商品类别目录
    public void categoryList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 使用redis优化。当categoryList存在与redis中时，直接取出
        Jedis jedis = JedisPoolUtils.getJedis();
        String categoryListStr = jedis.get("categoryList");
        List<Category> categoryList = null;

        // 若redis中不存在categoryList，则从数据库中取出，并存取redis以待下一次使用
        if (categoryList == null) {
            CategoryListService service = new CategoryListService();
            categoryList = service.findCategoryList();
            // 封装categoryList为json格式
            Gson gson = new Gson();
            categoryListStr = gson.toJson(categoryList);
        }

        // 将categoryListStr写入response中
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(categoryListStr);
    }
    
    //  显示首页的功能
    public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //  展示热门商品
        ProductService service = new ProductService();
        List<Product> hotProductList = service.findHotProductList();
        request.setAttribute("hotProductList", hotProductList);
        
        //  展示最新商品
        List<Product> newProductList = service.findNewProductList();
        request.setAttribute("newProductList", newProductList);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
    
    //  显示商品详细信息功能
    public void productInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
//                    System.out.println("pids:" + pids);
                }
            }
        }
        Cookie cookie = new Cookie("pids", pids);
        response.addCookie(cookie);
        
        
        request.getRequestDispatcher("/product_info.jsp").forward(request, response);
    }
    
    //  根据商品的类别显示商品的列表
    public void productListByCid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
    
    //  将商品加入购物车
    public void addProductToCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //  获得session对象中的cart对象
        HttpSession session = request.getSession();
        ProductService service = new ProductService();
        
        //  获得需要添加到购物车中的商品pid
        String pid = request.getParameter("pid");        
        //  获得该商品的购买量
        int buyNum = Integer.parseInt(request.getParameter("buyNum"));
        
        //  获得购买的商品对象product
        Product product = service.getProductInfoByPid(pid);
        //  计算商品的小计价格
        double subTotal = product.getShop_price() * buyNum;
        
        //  封装cartItem对象
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setBuyNum(buyNum);
        item.setSubTotal(subTotal);
        
        //  获得购物车对象，并判断购物车是否存在
        Cart cart = (Cart) session.getAttribute("cart");
        //  判断cart是否存在，若不存在，则创建它
        if (cart == null) {
            cart = new Cart();
        }
        //  获得cart中的购物车项列表
        Map<String, CartItem> cartItems = cart.getCartItems();
        
        //  判断所购买的product是否存在与cart中
        if (cartItems.containsKey(pid)) {
            //  如果存在，将原来的buyNum与新加入购物车的数量相加，原来的subTotal与新加入购物车的小计相加
            CartItem cartItem = cartItems.get(pid);
            cartItem.setBuyNum(cartItem.getBuyNum() + buyNum);
            cartItem.setSubTotal(cartItem.getSubTotal() + subTotal);
        } else {
            //  如果不存在，将cartItem放入cartItems
            cartItems.put(pid, item);
        }
        double total = cart.getTotal();
        total += subTotal;
        cart.setTotal(total);
        
        //  将新的购物车对象cart放入session中
        session.setAttribute("cart", cart);
        //  用重定向而不是转发是为了避免刷新cart.jsp页面时 重复加入购物车
        response.sendRedirect(request.getContextPath() + "/cart.jsp");       
        
    }
    
    //  删除购物车中的商品
    public void delProductFromCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pid = request.getParameter("pid");
        HttpSession session = request.getSession();
        ProductService service = new ProductService();
       
        Cart cart = (Cart) session.getAttribute("cart");
        Map<String, CartItem> cartItems = cart.getCartItems();
        if (cartItems.containsKey(pid)) {
            //  获得该商品项
            CartItem cartItem = cartItems.get(pid);
            //  计算新的总计价格total
            double minusSubTotal = cartItem.getSubTotal();
            double total = cart.getTotal();
            total -= minusSubTotal;
            cart.setTotal(total);
            //  删除该商品
            cartItems.remove(pid);
        }
        
        //  将删除过商品的cart放入session
        session.setAttribute("cart", cart);
        
        response.sendRedirect(request.getContextPath() + "/cart.jsp");
        
    }
    
    //  清空购物车
    public void clearCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("cart");
        response.sendRedirect(request.getContextPath() + "/cart.jsp");
    }
    
    //  提交商品订单
    public void submitOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        //  判断用户是否登录，若否，跳转到登录界面
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
        
        //  封装orders对象
        Orders orders = new Orders();
        //  1.订单号
        String uuid = CommonsUtils.getUUID();
        orders.setOid(uuid);
        //  2.下单时间
        orders.setOrdertime(new Date());
        //  3.订单总金额
        Cart cart = (Cart) session.getAttribute("cart");
        orders.setTotal(cart.getTotal());
        //  4.订单状态，1表示已付款，0表示未付款
        orders.setState(0);
        //  5.订单地址
        orders.setAddress(null);
        //  6.收货人姓名
        orders.setName(null);
        //  7.收货人电话
        orders.setTelephone(0);
        //  8.下单的用户
        orders.setUser(user);
        //  9.该订单中的订单项
        Map<String, CartItem> cartItems = cart.getCartItems();
        List<OrderItem> orderItems = orders.getOrderItems();
        //  至此行代码之前代码运行正常 。时间2017-11-13
        for (CartItem cartItem : cartItems.values()) {
            //  封装每个订单项
            OrderItem orderItem = new OrderItem();
            //  1.订单项的id
            orderItem.setItemid(CommonsUtils.getUUID());            
            //  2.订单项的数量
            orderItem.setCount(cartItem.getBuyNum());
            //  3.单个订单项的小计金额
            orderItem.setSubtotal(cartItem.getSubTotal());
            //  4.订单项包含的商品
            orderItem.setProduct(cartItem.getProduct());
            //  5.该订单项属于哪个订单
            orderItem.setOrders(orders);
            //  将封装好的orderItem添加到订单order中
            orderItems.add(orderItem);
        }
        //  将订单放入数据库中
        ProductService service = new ProductService();
        try {
            service.submitOrders(orders);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        session.setAttribute("orders", orders);
        
        //跳转到order_info.jsp页面
        response.sendRedirect(request.getContextPath() + "/order_info.jsp");
    }
    
    
        
    
    
}

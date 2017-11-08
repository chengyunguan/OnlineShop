package com.itheima.service;

import java.sql.SQLException;
import java.util.List;

import com.itheima.dao.ProductDao;
import com.itheima.domain.PageBean;
import com.itheima.domain.Product;

public class ProductService {

    public List<Product> findHotProductList() {
        ProductDao dao = new ProductDao();
        List<Product> hotProductList = null;
        try {
            hotProductList = dao.findHotProductList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotProductList;
    }

    public List<Product> findNewProductList() {
        ProductDao dao = new ProductDao();
        List<Product> newProductList = null;
        try {
            newProductList = dao.findNewProductList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newProductList;
    }
    
    //  按cid查询每页商品
    public PageBean<Product> findPoductListByCid(String cid, int currentPage, int currentCount) {
        
        
        //  封装pageBean
        PageBean<Product> pageBean = new PageBean<Product>();
        //  1.封装当前页
//        pageBean.setCurrentPage(currentPage);
        //  2.封装每页显示的条目数
        pageBean.setCurrentCount(currentCount);
        //  3.封装总条数
        ProductDao dao = new ProductDao();
        int totalCount = 0;
        try {
            totalCount = dao.getCount(cid);
            pageBean.setTotalCount(totalCount);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        //  4.封装总页数
        int totalPage = (int) Math.ceil(1.0 * totalCount / currentCount);
        pageBean.setTotalPage(totalPage);
        //  5.封装当前页显示的数据
        //  当前页的index
        int index = (currentPage - 1) * currentCount;
        List<Product> list = null;
        try {
            list = dao.findPoductListByCid(cid, index, currentCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pageBean.setList(list);
        return pageBean;
    }

}

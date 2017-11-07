package com.itheima.service;

import java.sql.SQLException;
import java.util.List;

import com.itheima.dao.CategoryListDao;
import com.itheima.domain.Category;

public class CategoryListService {

    public List<Category> findCategoryList() {
        CategoryListDao dao = new CategoryListDao();
        List<Category> categoryList = null;
        try {
            categoryList = dao.findCategoryList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryList;
    }

}

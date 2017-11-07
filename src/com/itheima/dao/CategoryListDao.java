package com.itheima.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.itheima.domain.Category;
import com.itheima.utils.DataSourceUtils;

public class CategoryListDao {

    public List<Category> findCategoryList() throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from category;";
        List<Category> categoryList = runner.query(sql, new BeanListHandler<Category>(Category.class));
        return categoryList;
    }

}

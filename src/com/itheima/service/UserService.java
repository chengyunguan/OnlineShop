package com.itheima.service;

import java.sql.SQLException;

import com.itheima.dao.UserDao;
import com.itheima.domain.User;

public class UserService {

	public boolean regist(User user) {
		
		UserDao dao = new UserDao();
		int row = 0;
		try {
			row = dao.regist(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return row>0?true:false;
	}

    public void active(String activeCode) throws SQLException {
        UserDao dao = new UserDao();
        dao.active(activeCode);
    }

    public boolean checkUsername(String username) throws SQLException {
        UserDao dao = new UserDao();
        Long isExist = dao.checkUsername(username);
        return isExist > 0 ? true : false;
    }

}

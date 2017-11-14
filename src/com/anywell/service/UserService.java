package com.anywell.service;

import com.anywell.dao.UserDao;
import com.anywell.domain.User;

public class UserService {
//注册用户
	public boolean register(User user){
		UserDao userDao=new UserDao();
		int row= userDao.register(user);
		return row>0?true:false;
	}
}

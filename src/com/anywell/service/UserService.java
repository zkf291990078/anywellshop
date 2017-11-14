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
	
	public void active(String code){
		UserDao userDao=new UserDao();
		userDao.active(code);
	}
	
	public boolean checkName(String username){
	UserDao userDao=new UserDao();
	long row=0;
	try {
	 row= userDao.checkName(username);
	} catch (Exception e) {
		// TODO: handle exception
	}
	
	return row>0?true:false;
	}
}

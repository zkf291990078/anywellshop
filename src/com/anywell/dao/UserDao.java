package com.anywell.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;

import com.anywell.domain.User;
import com.anywell.utils.DataSourceUtils;

public class UserDao {

	public int register(User user){
		QueryRunner queryRunner=new QueryRunner(DataSourceUtils.getDataSource());
		int row=0;
		try {
			row = queryRunner.update("insert into user values(?,?,?,?,?,?,?,?,?,?)",user.getUid(),user.getUsername(),user.getPassword(),user.getName(),user.getEmail(),user.getTelephone(),user.getBirthday(),user.getSex(),user.getState(),user.getCode());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return row;
	}
}

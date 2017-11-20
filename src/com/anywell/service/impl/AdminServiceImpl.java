package com.anywell.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.anywell.dao.AdminDao;
import com.anywell.domain.Category;
import com.anywell.domain.Order;
import com.anywell.domain.Product;
import com.anywell.service.AdminService;

public class AdminServiceImpl implements AdminService {
	public List<Category> findAllCategory() {
		AdminDao dao = new AdminDao();
		try {
			return dao.findAllCategory();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void saveProduct(Product product) {
		// TODO Auto-generated method stub
		AdminDao dao = new AdminDao();
		try {
			dao.saveProduct(product);
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	public List<Order> findAllOrders() {
		// TODO Auto-generated method stub
		AdminDao dao = new AdminDao();
		List<Order> ordersList = null;
		try {
			ordersList = dao.findAllOrders();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ordersList;
	}

	public List<Map<String, Object>> findOrderInfoByOid(String oid) {
		// TODO Auto-generated method stub
		AdminDao dao = new AdminDao();
		List<Map<String, Object>> orderInfos = null;
		try {
			orderInfos = dao.findOrderInfoByOid(oid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderInfos;
	}
}

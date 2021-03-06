package com.anywell.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.anywell.dao.ProductDao;
import com.anywell.domain.Category;
import com.anywell.domain.Order;
import com.anywell.domain.PageBean;
import com.anywell.domain.Product;
import com.anywell.utils.DataSourceUtils;

public class ProductService {

	public List<Product> findHotProducts() {
		ProductDao proDao = new ProductDao();
		List<Product> hotPros = null;
		try {
			hotPros = proDao.findHotProducts();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hotPros;
	}

	public List<Product> findNewProducts() {
		ProductDao proDao = new ProductDao();
		List<Product> newPros = null;
		try {
			newPros = proDao.findNewProducts();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newPros;
	}

	public List<Category> findAllCategory() {
		ProductDao dao = new ProductDao();
		List<Category> cateList = null;
		try {
			cateList = dao.findAllCategory();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cateList;
	}

	public PageBean<Product> findProuctsBycid(String cid, int currentPage, int currentCount) {
		ProductDao dao = new ProductDao();
		PageBean<Product> pageBean = new PageBean<Product>();
		pageBean.setCurrentCount(currentCount);
		pageBean.setCurrentPage(currentPage);
		int totalCount = 0;
		try {
			totalCount = dao.getCount(cid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pageBean.setTotalCount(totalCount);
		int totalPage = (int) Math.ceil(1.0 * totalCount / currentCount);
		pageBean.setTotalPage(totalPage);
		List<Product> products = null;
		int index = (currentPage - 1) * currentCount;
		try {
			products = dao.findProductsBycid(cid, index, currentCount);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pageBean.setList(products);
		return pageBean;
	}

	public Product findProductById(String pid) {
		// TODO Auto-generated method stub
		ProductDao dao = new ProductDao();
		Product pro = null;
		try {
			pro = dao.findProductById(pid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pro;
	}

	public void submitOrder(Order order) {
		// TODO Auto-generated method stub
		try {
			DataSourceUtils.startTransaction();
			ProductDao dao = new ProductDao();
			dao.saveOrder(order);
			dao.saveOrderItems(order);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				DataSourceUtils.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				DataSourceUtils.commitAndRelease();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void updateOrderAdrr(Order order) {
		ProductDao dao = new ProductDao();
		try {
			dao.updateOrderAdrr(order);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateOrderState(String r6_Order) {
		// TODO Auto-generated method stub
		ProductDao dao = new ProductDao();
		try {
			dao.updateOrderState(r6_Order);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Order> findAllOrders(String uid, int currentPage, int currentCount) {
		// TODO Auto-generated method stub
		ProductDao dao = new ProductDao();
		List<Order> orders = null;
		try {
			orders = dao.findAllOrders(uid, currentPage, currentCount);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orders;
	}

	public int getOrdersCount() {
		ProductDao dao = new ProductDao();
		int count = 0;
		try {
			count = dao.getOrdersCount();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	public List<Map<String, Object>> findAllOrderItems(String oid) {
		// TODO Auto-generated method stub
		ProductDao dao = new ProductDao();
		List<Map<String, Object>> orderItems = null;
		try {
			orderItems = dao.findAllOrderItems(oid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orderItems;
	}
}

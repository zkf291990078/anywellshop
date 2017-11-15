package com.anywell.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.fileupload.ParameterParser;

import com.anywell.dao.ProductDao;
import com.anywell.domain.Category;
import com.anywell.domain.Product;

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

	public List<Product> findProuctsBycid(String cid, int index, int pageCount) {
		ProductDao dao = new ProductDao();
		List<Product> products = null;
		try {
			products = dao.findProductsBycid(cid, index, pageCount);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return products;
	}

}

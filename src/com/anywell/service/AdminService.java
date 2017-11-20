package com.anywell.service;

import java.util.List;
import java.util.Map;

import com.anywell.domain.Category;
import com.anywell.domain.Order;
import com.anywell.domain.Product;

public interface AdminService {

	public List<Category> findAllCategory();

	public void saveProduct(Product product);

	public List<Order> findAllOrders();

	public List<Map<String, Object>> findOrderInfoByOid(String oid);

}

package com.anywell.web.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anywell.domain.Category;
import com.anywell.domain.Order;
import com.anywell.service.AdminService;
import com.anywell.utils.BeanFactory;
import com.google.gson.Gson;

/**
 * Servlet implementation class AdminServlet
 */
public class AdminServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	public void findAllCategory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 提供一个List<Category> 转成json字符串
		// AdminService service = (AdminService)
		AdminService service = (AdminService) BeanFactory.getBean("adminService");
		// AdminService service = new AdminService();
		List<Category> categoryList = service.findAllCategory();

		Gson gson = new Gson();
		String json = gson.toJson(categoryList);

		response.setContentType("text/html;charset=UTF-8");

		response.getWriter().write(json);

	}

	public void findAllOrders(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		AdminService service = (AdminService) BeanFactory.getBean("adminService");
		// AdminService service = new AdminService();
		List<Order> orderList = service.findAllOrders();

		request.setAttribute("orderList", orderList);

		request.getRequestDispatcher("/admin/order/list.jsp").forward(request, response);

	}

	public void findOrderInfoByOid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 获得oid
		String oid = request.getParameter("oid");

		// 用解耦合的方式进行编码----解web层与service层的耦合
		// 使用工厂+反射+配置文件
		AdminService service = (AdminService) BeanFactory.getBean("adminService");
		// AdminService service = new AdminService();
		List<Map<String, Object>> mapList = service.findOrderInfoByOid(oid);

		Gson gson = new Gson();
		String json = gson.toJson(mapList);
		// System.out.println(json);
		/*
		 * [ {"shop_price":4499.0,"count":2,"pname":"联想（Lenovo）小新V3000经典版",
		 * "pimage":"products/1/c_0034.jpg","subtotal":8998.0},
		 * {"shop_price":2599.0,"count":1,"pname":"华为 Ascend Mate7"
		 * ,"pimage":"products/1/c_0010.jpg","subtotal":2599.0} ]
		 */
		response.setContentType("text/html;charset=UTF-8");

		response.getWriter().write(json);
	}

}

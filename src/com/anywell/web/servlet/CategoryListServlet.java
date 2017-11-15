package com.anywell.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anywell.domain.Category;
import com.anywell.service.ProductService;
import com.anywell.utils.JedisPoolUtils;
import com.google.gson.Gson;

import redis.clients.jedis.Jedis;

/**
 * Servlet implementation class CategoryListServlet
 */
public class CategoryListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CategoryListServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		ProductService productService = new ProductService();
		// Jedis jedis= JedisPoolUtils.getJedis();
		// String categoryListJson= jedis.get("categorylist");
		// if(categoryListJson==null){
		List<Category> categories = productService.findAllCategory();
		Gson gson = new Gson();
		String categoryListJson = gson.toJson(categories);
		// jedis.set("categorylist",categoryListJson );
		// }
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(categoryListJson);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

package com.anywell.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anywell.domain.PageBean;
import com.anywell.domain.Product;
import com.anywell.service.ProductService;
import com.anywell.service.UserService;

/**
 * Servlet implementation class ProductListBycidServlet
 */
public class ProductListBycidServlet extends HttpServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductListBycidServlet() {
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
		String cid = request.getParameter("cid");
		ProductService productService = new ProductService();
		PageBean pageBean = new PageBean();
		List<Product> products = productService.findProuctsBycid(cid, 0, 12);
		pageBean.setCurrentCount(12);
		pageBean.setCurrentPage(1);
		pageBean.setList(products);
		request.setAttribute("pageBean", pageBean);
		request.getRequestDispatcher("/product_list.jsp").forward(request, response);
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

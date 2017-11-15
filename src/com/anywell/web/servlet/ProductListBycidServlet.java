package com.anywell.web.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anywell.domain.PageBean;
import com.anywell.domain.Product;
import com.anywell.service.ProductService;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

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
		String currentPage = request.getParameter("currentPage");
		int currentCount = 12;
		if (currentPage == null) {
			currentPage = "1";
		}
		int page = Integer.parseInt(currentPage);
		ProductService productService = new ProductService();
		PageBean<Product> pageBean = productService.findProuctsBycid(cid, page, currentCount);
		Cookie[] cookies = request.getCookies();
		ArrayList<Product> history = new ArrayList<>();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("pid")) {
					String pidstr = cookie.getValue();
					String[] pids = pidstr.split("-");
					for (String pid : pids) {
						Product product = productService.findProductById(pid);
						history.add(product);
					}
				}
			}
		}
		request.setAttribute("history", history);
		request.setAttribute("pageBean", pageBean);
		request.setAttribute("cid", cid);
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

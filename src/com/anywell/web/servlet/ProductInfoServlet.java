package com.anywell.web.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anywell.domain.Product;
import com.anywell.service.ProductService;

/**
 * Servlet implementation class ProductInfoServlet
 */
public class ProductInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String pid = request.getParameter("pid");
		String cid = request.getParameter("cid");
		String currentPage = request.getParameter("currentPage");
		ProductService productService = new ProductService();
		Product product = productService.findProductById(pid);
		Cookie[] cookies = request.getCookies();
		LinkedList<String> pidList = new LinkedList<>();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("pid")) {
					String pidstr = cookie.getValue();
					String[] pids = pidstr.split("-");
					pidList.addAll(Arrays.asList(pids));
					if (pidList.contains(pid)) {
						pidList.remove(pid);
					}
				}
			}
		}
		pidList.addFirst(pid);
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < pidList.size() && i < 9; i++) {
			sbf.append(pidList.get(i));
			sbf.append("-");
		}
		Cookie ck = new Cookie("pid", sbf.toString().substring(0, sbf.length() - 1));
		response.addCookie(ck);
		request.setAttribute("product", product);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("cid", cid);
		request.getRequestDispatcher("/product_info.jsp").forward(request, response);
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

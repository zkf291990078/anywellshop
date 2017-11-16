package com.anywell.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.anywell.domain.Cart;
import com.anywell.domain.CartItem;
import com.anywell.domain.Category;
import com.anywell.domain.PageBean;
import com.anywell.domain.Product;
import com.anywell.service.ProductService;
import com.google.gson.Gson;

/**
 * Servlet implementation class ProductServlet
 */
public class ProductServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	//
	// /**
	// * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	// response)
	// */
	// protected void doGet(HttpServletRequest request, HttpServletResponse
	// response) throws ServletException, IOException {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// /**
	// * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	// response)
	// */
	// protected void doPost(HttpServletRequest request, HttpServletResponse
	// response) throws ServletException, IOException {
	// // TODO Auto-generated method stub
	// doGet(request, response);
	// }

	// 清空购物车
	 public void clearCart(HttpServletRequest request, HttpServletResponse
	 response) throws ServletException, IOException {
	 HttpSession session = request.getSession();
	 session.removeAttribute("cart");
	
	 //跳转回cart.jsp
	 response.sendRedirect(request.getContextPath()+"/cart.jsp");
	
	 }

	// 删除单一商品
	 public void delProFromCart(HttpServletRequest request,
	 HttpServletResponse response) throws ServletException, IOException {
	 //获得要删除的item的pid
	 String pid = request.getParameter("pid");
	 //删除session中的购物车中的购物项集合中的item
	 HttpSession session = request.getSession();
	 Cart cart = (Cart) session.getAttribute("cart");
	 if(cart!=null){
	 Map<String, CartItem> cartItems = cart.getCartItems();
	 //需要修改总价
	 cart.setTotalPrice(cart.getTotalPrice()-cartItems.get(pid).getSubTotal());
	 //删除
	 cartItems.remove(pid);
	 cart.setCartItems(cartItems);
	
	 }
	
	 session.setAttribute("cart", cart);
	
	 //跳转回cart.jsp
	 response.sendRedirect(request.getContextPath()+"/cart.jsp");
	 }

	// 将商品添加到购物车
	public void addProductToCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pid = request.getParameter("pid");
		int buyNum = Integer.parseInt(request.getParameter("buyNum"));
		HttpSession session= request.getSession();
		ProductService service=new ProductService();
		Product product= service.findProductById(pid);
		CartItem item=new CartItem();
		item.setBuyNum(buyNum);
		item.setProduct(product);
		double subTotal=product.getShop_price()*buyNum;
		item.setSubTotal(subTotal);
		
		Cart cart=(Cart) session.getAttribute("cart");
		if(cart!=null){
			if(cart.getCartItems().containsKey(product.getPid())){
				cart.getCartItems().get(pid).setBuyNum(cart.getCartItems().get(pid).getBuyNum()+buyNum);
				cart.getCartItems().get(pid).setSubTotal(cart.getCartItems().get(pid).getSubTotal()+subTotal);
				cart.setTotalPrice(cart.getTotalPrice()+subTotal);
			}else{
				cart.getCartItems().put(pid, item);
				cart.setTotalPrice(cart.getTotalPrice()+subTotal);
			}
		}else{
			cart=new Cart();
			cart.getCartItems().put(pid, item);
			cart.setTotalPrice(subTotal);
		}
		session.setAttribute("cart", cart);
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}

	// 显示商品的类别的的功能
	public void categoryList(HttpServletRequest request, HttpServletResponse response)
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

	// 显示首页的功能
	// 显示商品的类别的的功能
	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ProductService productService = new ProductService();
		List<Product> hotProducts = productService.findHotProducts();
		List<Product> newProducts = productService.findNewProducts();
		request.setAttribute("hotProducts", hotProducts);
		request.setAttribute("newProducts", newProducts);
		request.getRequestDispatcher("/index.jsp").forward(request, response);

	}

	// 显示商品的详细信息功能
	public void productInfo(HttpServletRequest request, HttpServletResponse response)
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

	// 根据商品的类别获得商品的列表
	public void productListBycid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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

}

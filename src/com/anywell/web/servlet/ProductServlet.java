package com.anywell.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.anywell.domain.Cart;
import com.anywell.domain.CartItem;
import com.anywell.domain.Category;
import com.anywell.domain.Order;
import com.anywell.domain.OrderItem;
import com.anywell.domain.PageBean;
import com.anywell.domain.Product;
import com.anywell.domain.User;
import com.anywell.service.ProductService;
import com.anywell.utils.CommonsUtils;
import com.anywell.utils.PaymentUtil;
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
	
	//确认订单---更新收获人信息+在线支付
		public void confirmOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

			//1、更新收货人信息
			Map<String, String[]> properties = request.getParameterMap();
			Order order = new Order();
			try {
				BeanUtils.populate(order, properties);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}

			ProductService service = new ProductService();
			service.updateOrderAdrr(order);

			//2、在线支付
			/*if(pd_FrpId.equals("ABC-NET-B2C")){
				//介入农行的接口
			}else if(pd_FrpId.equals("ICBC-NET-B2C")){
				//接入工行的接口
			}*/
			//.......

			//只接入一个接口，这个接口已经集成所有的银行接口了  ，这个接口是第三方支付平台提供的
			//接入的是易宝支付
			// 获得 支付必须基本数据
			String orderid = request.getParameter("oid");
			//String money = order.getTotal()+"";//支付金额
			String money = "0.01";//支付金额
			// 银行
			String pd_FrpId = request.getParameter("pd_FrpId");

			// 发给支付公司需要哪些数据
			String p0_Cmd = "Buy";
			String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
			String p2_Order = orderid;
			String p3_Amt = money;
			String p4_Cur = "CNY";
			String p5_Pid = "";
			String p6_Pcat = "";
			String p7_Pdesc = "";
			// 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
			// 第三方支付可以访问网址
			String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("callback");
			String p9_SAF = "";
			String pa_MP = "";
			String pr_NeedResponse = "1";
			// 加密hmac 需要密钥
			String keyValue = ResourceBundle.getBundle("merchantInfo").getString(
					"keyValue");
			String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
					p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
					pd_FrpId, pr_NeedResponse, keyValue);


			String url = "https://www.yeepay.com/app-merchant-proxy/node?pd_FrpId="+pd_FrpId+
					"&p0_Cmd="+p0_Cmd+
					"&p1_MerId="+p1_MerId+
					"&p2_Order="+p2_Order+
					"&p3_Amt="+p3_Amt+
					"&p4_Cur="+p4_Cur+
					"&p5_Pid="+p5_Pid+
					"&p6_Pcat="+p6_Pcat+
					"&p7_Pdesc="+p7_Pdesc+
					"&p8_Url="+p8_Url+
					"&p9_SAF="+p9_SAF+
					"&pa_MP="+pa_MP+
					"&pr_NeedResponse="+pr_NeedResponse+
					"&hmac="+hmac;

			//重定向到第三方支付平台
			response.sendRedirect(url);


		}
	
	
	public void submitOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}
		Order order = new Order();
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart == null) {
			throw new RuntimeException("购物车没有数据");
		}
		order.setAddress(null);
		order.setName(null);
		order.setOid(CommonsUtils.getUUID());
		for (Entry<String, CartItem> entry : cart.getCartItems().entrySet()) {
			CartItem cartItem = entry.getValue();
			OrderItem orderItem = new OrderItem();
			orderItem.setCount(cartItem.getBuyNum());
			orderItem.setItemid(CommonsUtils.getUUID());
			orderItem.setOrder(order);
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setSubtotal(cartItem.getSubTotal());
			order.getOrderItems().add(orderItem);
		}
		Date date=new Date();                             
        SimpleDateFormat temp=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
        String date2=temp.format(date);  
        Date date3=null;
		try {
			date3 = temp.parse(date2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
       order.setOrdertime(date3);
		order.setState(0);
		order.setTelephone(null);
		order.setTotal(cart.getTotalPrice());
		order.setUser(user);
		ProductService service=new ProductService();
		service.submitOrder(order);
		request.getSession().setAttribute("order", order);
		response.sendRedirect(request.getContextPath()+"/order_info.jsp");
	}

	// 清空购物车
	public void clearCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.removeAttribute("cart");

		// 跳转回cart.jsp
		response.sendRedirect(request.getContextPath() + "/cart.jsp");

	}

	// 删除单一商品
	public void delProFromCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获得要删除的item的pid
		String pid = request.getParameter("pid");
		// 删除session中的购物车中的购物项集合中的item
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart != null) {
			Map<String, CartItem> cartItems = cart.getCartItems();
			// 需要修改总价
			cart.setTotalPrice(cart.getTotalPrice() - cartItems.get(pid).getSubTotal());
			// 删除
			cartItems.remove(pid);
			cart.setCartItems(cartItems);

		}

		session.setAttribute("cart", cart);

		// 跳转回cart.jsp
		response.sendRedirect(request.getContextPath() + "/cart.jsp");
	}

	// 将商品添加到购物车
	public void addProductToCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pid = request.getParameter("pid");
		int buyNum = Integer.parseInt(request.getParameter("buyNum"));
		HttpSession session = request.getSession();
		ProductService service = new ProductService();
		Product product = service.findProductById(pid);
		CartItem item = new CartItem();
		item.setBuyNum(buyNum);
		item.setProduct(product);
		double subTotal = product.getShop_price() * buyNum;
		item.setSubTotal(subTotal);

		Cart cart = (Cart) session.getAttribute("cart");
		if (cart != null) {
			if (cart.getCartItems().containsKey(product.getPid())) {
				cart.getCartItems().get(pid).setBuyNum(cart.getCartItems().get(pid).getBuyNum() + buyNum);
				cart.getCartItems().get(pid).setSubTotal(cart.getCartItems().get(pid).getSubTotal() + subTotal);
				cart.setTotalPrice(cart.getTotalPrice() + subTotal);
			} else {
				cart.getCartItems().put(pid, item);
				cart.setTotalPrice(cart.getTotalPrice() + subTotal);
			}
		} else {
			cart = new Cart();
			cart.getCartItems().put(pid, item);
			cart.setTotalPrice(subTotal);
		}
		session.setAttribute("cart", cart);
		response.sendRedirect(request.getContextPath() + "/cart.jsp");
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

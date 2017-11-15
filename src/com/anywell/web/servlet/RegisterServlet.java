package com.anywell.web.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.anywell.domain.User;
import com.anywell.service.UserService;
import com.anywell.utils.CommonsUtils;
import com.anywell.utils.MailUtils;

/**
 * Servlet implementation class RegisterServlet
 */
public class RegisterServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		Map<String, String[]> properties = request.getParameterMap();
		User user = new User();
		ConvertUtils.register(new Converter() {
			@Override
			public Object convert(Class arg0, Object arg1) {
				// TODO Auto-generated method stub
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date parse = null;
				try {
					parse = format.parse(arg1.toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return parse;
			}
		}, Date.class);
		try {
			BeanUtils.populate(user, properties);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		user.setUid(CommonsUtils.getUUID());
		user.setTelephone(null);
		String code=CommonsUtils.getUUID();
		user.setCode(code);
		user.setState(0);
		UserService userService=new UserService();
	   boolean isRegisterSuccess=	userService.register(user);
	   if(isRegisterSuccess){
		   String emailMsg="激活用户<a href='http://localhost:8080/anywellShop/active?activeCode="+user.getCode()+
				   "'>http://localhost:8080/anywellShop/active?activeCode="+user.getCode()+"</a>";
		   try {
			MailUtils.sendMail(user.getEmail(), emailMsg);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   response.sendRedirect(request.getContextPath()+"/registerSuccess.jsp");
	   }else{
		   response.sendRedirect(request.getContextPath()+"/registerFail.jsp");
	   };
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

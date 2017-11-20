package com.anywell.web.servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.anywell.domain.Category;
import com.anywell.domain.Product;
import com.anywell.service.AdminService;
import com.anywell.utils.BeanFactory;
import com.anywell.utils.CommonsUtils;

/**
 * Servlet implementation class SaveProductServlet
 */
public class SaveProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		Product product = new Product();
		Category category = new Category();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload fileUpload = new ServletFileUpload(factory);
		try {
			List<FileItem> fileItems = fileUpload.parseRequest(request);
			for (FileItem fileItem : fileItems) {
				if (fileItem.isFormField()) {
					String fieldName = fileItem.getFieldName();
					String fieldValue = fileItem.getString("utf-8");
					map.put(fieldName, fieldValue);
				} else {
					String path = request.getServletContext().getRealPath("upload");
					InputStream inputStream = fileItem.getInputStream();
					OutputStream outputStream = new FileOutputStream(path + "/" + fileItem.getName());
					IOUtils.copy(inputStream, outputStream);
					inputStream.close();
					outputStream.close();
					fileItem.delete();
					map.put("pimage", "upload/" + fileItem.getName());
				}
			}
			BeanUtils.populate(product, map);
			product.setPid(CommonsUtils.getUUID());
			Date currentTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = formatter.format(currentTime);
			ParsePosition pos = new ParsePosition(0);
			Date currentTime_2 = formatter.parse(dateString, pos);
			product.setPdate(currentTime);
			category.setCid(map.get("cid").toString());
			product.setCategory(category);
			product.setPflag(0);
			AdminService service = (AdminService) BeanFactory.getBean("adminService");
			// service.saveProduct(product);
			response.sendRedirect("/list.jsp");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

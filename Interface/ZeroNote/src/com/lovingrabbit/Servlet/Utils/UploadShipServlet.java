package com.lovingrabbit.Servlet.Utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import net.sf.json.JSONObject;

public class UploadShipServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String path;
	@SuppressWarnings("deprecation")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("utf-8"); // 设置编码
		// 获得磁盘文件条目工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 获取文件需要上传到的路径
		String path = req.getRealPath("/upload");
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		factory.setRepository(new File(path));
		// 设置 缓存的大小
		factory.setSizeThreshold(1024 * 1024);
		// 文件上传处理
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			// 可以上传多个文件
			List<FileItem> list = upload
					.parseRequest(new ServletRequestContext(req));
			for (FileItem item : list) {
				// 获取属性名字
				String name = item.getFieldName();
				// 如果获取的 表单信息是普通的 文本 信息
				if (item.isFormField()) {
					// 获取用户具体输入的字符串,因为表单提交过来的是 字符串类型的
					String value = item.getString();
					req.setAttribute(name, value);
				} else {
					// 获取路径名
					String value = item.getName();
					// 索引到最后一个反斜杠
					int start = value.lastIndexOf("\\");
					// 截取 上传文件的 字符串名字，加1是 去掉反斜杠，
					String filename = value.substring(start + 1);
					req.setAttribute(name, filename);
					// 写到磁盘上
					item.write(new File(path, filename));// 第三方提供的
					System.out.println("上传成功：" + filename);
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("upload", filename);
					resp.getWriter().print(jsonObject.toString());// 将路径返回给客户端
				}
			}

		} catch (Exception e) {
			System.out.println("上传失败");
			resp.getWriter().print("上传失败：" + e.getMessage());
		}
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
}
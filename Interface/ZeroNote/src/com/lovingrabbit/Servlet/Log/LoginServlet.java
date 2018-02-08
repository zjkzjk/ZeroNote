package com.lovingrabbit.Servlet.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.lovingrabbit.Servlet.Utils.Untils;

public class LoginServlet extends HttpServlet{
	String truePassword = null;
	ResultSet rs;
	String returnJSon, mobile, pass;
	int logtype;
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("utf8");
		StringBuffer jb = new StringBuffer();
		String line = null;
		String result = "";
		try {
			// StringBuffer读入字节流
			BufferedReader reader = req.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);

		} catch (Exception e) {
			/* report an error */ }

		try {
			// JsonObject解析
			JSONObject jsonObject = new JSONObject(jb.toString());
			mobile = jsonObject.getString("mobile");
			pass = jsonObject.getString("password");
			logtype = jsonObject.getInt("logtype");
			
		} catch (Exception e) {
			// crash and burn
			throw new IOException("Error parsing JSON request string");
		}
		Untils untils = new Untils();
		String sql = "select PASSWORD FROM user_infro WHERE mobile = " + mobile;
		try {
			rs = untils.select(sql);
			while (rs.next()) {
				truePassword = rs.getString("password");
//				System.out.println(truePassword);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (truePassword == null) {
			returnJSon = "{\"result\": \"" + 0 + "\" }";
		} else if (truePassword.equals(pass)) {
			returnJSon = "{\"result\": \"" + 1 + "\" }";
		} else {
			returnJSon = "{\"result\": \"" + 2 + "\" }";
		}
		JSONObject rjson = new JSONObject(returnJSon);
		resp.setHeader("content-type", "application/json;charset=utf-8");
		resp.setCharacterEncoding("utf8");
		PrintWriter out = resp.getWriter();
		out.println(rjson);
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
}

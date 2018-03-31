package com.lovingrabbit.Servlet.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.lovingrabbit.Servlet.Utils.Untils;

public class RegisterServlet extends HttpServlet{
	String truePassword = null;
	ResultSet rs;
	String returnJSon, mobile, pass, nikename;
	int registertype;
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("utf8");
		StringBuffer jb = new StringBuffer();
		returnJSon = "";
		truePassword = "";
		String line = null;
		String result = "";
		String intro = "懒得写简介";
		String birth = "1996-01-01";
		String year = "1996";
		try {
			// StringBuffer读取输入输出留
			BufferedReader reader = req.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);

		} catch (Exception e) {
			/* report an error */ }
		System.out.println("json:" + URLDecoder.decode(jb.toString(), "utf-8"));
		try {
			// JSonObject 解析
			JSONObject jsonObject = new JSONObject(jb.toString());
			mobile = jsonObject.getString("mobile");
			pass = jsonObject.getString("password");
			nikename = jsonObject.getString("username");
			registertype = jsonObject.getInt("registertype");

		} catch (Exception e) {
			// crash and burn
			throw new IOException("Error parsing JSON request string");
		}
		String selectSQL = "select PASSWORD FROM user_infro WHERE mobile =" + mobile;
		String insertSQL = "INSERT INTO user_infro(username, password,pic,mobile,birth)"
				+ " VALUES(\"" + nikename + "\",\"" + pass + "\"," + "\"/pic\"" 
				+ ",\"" + mobile + "\",\"" + birth +"\")";
		System.out.println(selectSQL);
		Untils untils = new Untils();
		try {
			rs = untils.select(selectSQL);
			while (rs.next()) {
				truePassword = rs.getString("password");
				System.out.println(truePassword);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!truePassword.equals("")) {
			returnJSon = "{'result':" + 2 + "}";
		} else {
			try {
				System.out.println(insertSQL);
				untils.insert(insertSQL);
				returnJSon = "{'result':" + 1 + "}";
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				returnJSon = "{'result':" + 0 + "}";
			}
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

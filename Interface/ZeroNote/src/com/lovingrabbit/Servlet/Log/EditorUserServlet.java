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

public class EditorUserServlet extends HttpServlet{
	String mobile,username,pic,birth,returnJSon;
	ResultSet rs;
	int id;
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
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
			/* report an error */ 
		}
		try {
			// JsonObject解析
			JSONObject jsonObject = new JSONObject(jb.toString());
			mobile = jsonObject.getString("mobile");
			username = jsonObject.getString("username");
			pic = jsonObject.getString("pic");
			birth = jsonObject.getString("birth");
			id = jsonObject.getInt("id");
			
		} catch (Exception e) {
			// crash and burn
			throw new IOException("Error parsing JSON request string");
		}
		String updateSql = "update user_infro set username = \""+username+"\" , pic = \"" +pic +"\" , birth = \""+birth
				+"\" where mobile = "+mobile;
		Untils untils = new Untils();
		try {
			rs = untils.select(updateSql);
			returnJSon = "{'result':" + 1 + "}";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			returnJSon = "{'result':" + 2 + "}";
		}
		JSONObject rjson = new JSONObject(returnJSon);
		resp.setHeader("content-type", "application/json;charset=utf-8");
		resp.setCharacterEncoding("utf8");
		PrintWriter out = resp.getWriter();
		out.println(rjson);
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

}

package com.lovingrabbit.Servlet.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lovingrabbit.Servlet.Utils.Untils;

import net.sf.json.JSONObject;

public class GetUserServlet extends HttpServlet{
	ResultSet rs;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String usermobile = req.getParameter("mobile");
		String selectUser= "select * from user_infro where mobile="+ usermobile;
		Untils untils = new Untils();
		JSONObject jsonObject = new JSONObject();;
		try {
			rs = untils.select(selectUser);
			while (rs.next()) {
				String username = rs.getString("username");
				String pic = rs.getString("pic");
				String birth = rs.getString("birth");
				int id = rs.getInt("id");
				jsonObject.put("id", id);
				jsonObject.put("username", username);
				jsonObject.put("pic", pic);
				jsonObject.put("birth", birth);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resp.setHeader("content-type", "application/json;charset=utf-8");
		resp.setCharacterEncoding("utf8");
		PrintWriter out = resp.getWriter();
		out.println(jsonObject);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
}

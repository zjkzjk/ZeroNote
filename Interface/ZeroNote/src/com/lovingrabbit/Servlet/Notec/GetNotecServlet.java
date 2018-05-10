package com.lovingrabbit.Servlet.Notec;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.lovingrabbit.Servlet.Utils.Untils;

public class GetNotecServlet extends HttpServlet{
	ResultSet rSet,rs,rl;
	int user_id,sum;
	JSONObject jsonObject;
	JSONArray jsonArray;
	int notec_id,count;
	String notec_name,notec_desc,createTime,updateTime,body,pic;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String usermobile = req.getParameter("mobile");
		String selectsId = "select id from user_infro where mobile = "+ usermobile;
		Untils untils = new Untils();
		count = 0;
		sum = 0;
		jsonArray = new JSONArray();
		try {
			rSet = untils.select(selectsId);
			while (rSet.next()) {
				user_id = rSet.getInt("id");
			}
			String selectNotec = "select * from note_class where user_id =" + user_id;
			rs = untils.select(selectNotec);
			while (rs.next()) {
				notec_id = rs.getInt("notec_id");
				String selectNotecCount = "select count(*) as sum from note where notec_id =" + notec_id + " order by updatetime asc";
				rl = untils.select(selectNotecCount);
				notec_name = rs.getString("notec_name");
				notec_desc = rs.getString("notec_desc");
				createTime = rs.getString("createtime");
				updateTime = rs.getString("updatetime");
				body = rs.getString("body");
				pic = rs.getString("pic");
				jsonObject = new JSONObject();
				jsonObject.put("notec_id", notec_id);
				jsonObject.put("notec_name", notec_name);
				jsonObject.put("notec_desc", notec_desc);
				jsonObject.put("createtime", createTime);
				jsonObject.put("updatetime", updateTime);
				jsonObject.put("body", body);
				jsonObject.put("pic", pic);
				while (rl.next()) {
					sum = rl.getInt("sum");
				}
				jsonObject.put("sum", sum);
				jsonArray.add(count,jsonObject);
				count = count + 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject returnJSon = new JSONObject();
		returnJSon.put("search", jsonArray);
		returnJSon.put("allCount", count);
		resp.setHeader("content-type", "application/json;charset=utf-8");
		resp.setCharacterEncoding("utf8");
		PrintWriter out = resp.getWriter();
		out.println(returnJSon);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

}

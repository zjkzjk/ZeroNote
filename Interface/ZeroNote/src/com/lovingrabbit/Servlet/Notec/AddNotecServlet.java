package com.lovingrabbit.Servlet.Notec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.lovingrabbit.Servlet.Utils.Untils;

public class AddNotecServlet extends HttpServlet{
	String truePassword = null;
	ResultSet rs;
	int id;
	String returnJSon, mobile, notec_name, notec_desc;
	String createTime, updateTime;
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
			notec_name = jsonObject.getString("notec_name");
			notec_desc = jsonObject.getString("notec_desc");
			
		} catch (Exception e) {
			// crash and burn
			throw new IOException("Error parsing JSON request string");
		}
		Untils untils = new Untils();
		String selectSql = "select id from user_infro where mobile = " + mobile;
		Date date = new Date();
		createTime = dateToString(date);
		updateTime = dateToString(date);
		try {
			rs = untils.select(selectSql);
			while (rs.next()) {
				id = rs.getInt("id");
			}
			String addSql = "insert into note_class(user_id,notec_name,notec_desc,createtime,updatetime,pic,body)"
					+"values(\""+id+"\",\""+ notec_name +"\",\""+ notec_desc+"\",\""+createTime+"\",\""+
					updateTime+"\",\""+"/pic"+"\","+0+")";
			System.out.println(addSql);
			untils.insert(addSql);
			returnJSon = "{'result':" + 1 + "}";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			returnJSon = "{'result':" + 0 + "}";
		}
		JSONObject rjson = new JSONObject(returnJSon);
		resp.setHeader("content-type", "application/json;charset=utf-8");
		resp.setCharacterEncoding("utf8");
		PrintWriter out = resp.getWriter();
		out.println(rjson);
		
	}
	public static String dateToString(Date time) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ctime = formatter.format(time);
		return ctime;
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

}

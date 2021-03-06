package com.lovingrabbit.Servlet.Note;

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

public class AddNoteServlet extends HttpServlet{
	String mobile,note_title,note_body,note_tag,location;
	int note_type,ifshare,notec_id,user_id;
	ResultSet rs;
	String createTime, updateTime,returnJSon;
	
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
			/* report an error */ 
		}
		try {
			// JsonObject解析
			JSONObject jsonObject = new JSONObject(jb.toString());
			mobile = jsonObject.getString("mobile");
			notec_id = jsonObject.getInt("notec_id");
			note_title = jsonObject.getString("note_title");
			note_body = jsonObject.getString("note_body");
			note_tag = jsonObject.getString("note_tag");
			location = jsonObject.getString("location");
			ifshare = jsonObject.getInt("ifshare");
			note_type = jsonObject.getInt("note_type");
			
		} catch (Exception e) {
			// crash and burn
			throw new IOException("Error parsing JSON request string");
		}
		Date date = new Date();
		createTime = dateToString(date);
		updateTime = dateToString(date);
		String selectSql = "select id from user_infro where mobile ="+mobile;
		Untils untils = new Untils();
		try {
			rs = untils.select(selectSql);
			while (rs.next()) {
				user_id = rs.getInt("id");
			}
			String insertSql = "insert into note(notec_id,user_id,notet_id,note_title,note_body,createtime,updatetime,"
					+ "note_tag,ifshare,location) values(" + notec_id +","+user_id+","+note_type+",\""+note_title+"\",\""+
					note_body+"\",\""+createTime+"\",\""+updateTime+"\",\""+note_tag+"\","+ifshare+",\""+location+"\""+")";
			
			untils.insert(insertSql);
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

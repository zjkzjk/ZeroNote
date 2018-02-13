package com.lovingrabbit.Servlet.Notec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.lovingrabbit.Servlet.Utils.Untils;

public class EditorNotecServlet extends HttpServlet{
	int notec_id;
	String notec_name,notec_desc,pic,updatetime,returnJSon;
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
			notec_id = jsonObject.getInt("notec_id");
			notec_name = jsonObject.getString("notec_name");
			notec_desc = jsonObject.getString("notec_desc");
			pic = jsonObject.getString("pic");
		} catch (Exception e) {
			// crash and burn
			throw new IOException("Error parsing JSON request string");
		}
		Untils untils = new Untils();
		Date date = new Date();
		updatetime = dateToString(date);
		String updateSql = "update note_class set notec_name=\""+ notec_name +"\",notec_desc=\""
				+ notec_desc +"\",pic=\""+ pic +"\",updatetime=\""+ updatetime +"\" where notec_id="+notec_id;
		try {
			System.out.println(updateSql);
			untils.update(updateSql);
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

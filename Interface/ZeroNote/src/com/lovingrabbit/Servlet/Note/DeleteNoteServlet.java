package com.lovingrabbit.Servlet.Note;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.lovingrabbit.Servlet.Utils.Untils;

import org.json.JSONArray;

public class DeleteNoteServlet extends HttpServlet{
	String returnJSon;
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("utf8");
		StringBuffer jb = new StringBuffer();
		StringBuffer deleteUrl= new StringBuffer();
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
		
			deleteUrl.append("delete from note where note_id in(");
			JSONObject jsonObject = new JSONObject(jb.toString());
			JSONArray jsonArray = jsonObject.getJSONArray("delete_id");
			for (int i = 0;i<jsonArray.length();i++) {
				JSONObject getJson = jsonArray.getJSONObject(i);
				int id = getJson.getInt("id");
				if (i==jsonArray.length()-1) {
					deleteUrl.append(id+")");
				}else {
					deleteUrl.append(id+",");
				}
			}
			
		} catch (Exception e) {
			// crash and burn
			throw new IOException("Error parsing JSON request string");
		}
		System.out.println(deleteUrl.toString());
		Untils untils = new Untils();
		try {
			untils.delete(deleteUrl.toString());
			returnJSon = "{'result':" + 1 + "}";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			returnJSon = "{'result':" + 2+ "}";
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

package com.lovingrabbit.Servlet.Note;

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

public class GetOneNoteServlet extends HttpServlet{
	ResultSet rs;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String note_id = req.getParameter("note_id");
		String SelectOneNote = "select * from note where note_id = "+note_id;
		Untils untils = new Untils();
		JSONObject jsonObject = new JSONObject();
		try {
			rs = untils.select(SelectOneNote);
			while (rs.next()) {
				
				int notec_id = rs.getInt("notec_id");
				String note_title = rs.getString("note_title");
				String note_body = rs.getString("note_body");
				String createTime = rs.getString("createtime");
				String updateTime = rs.getString("updatetime");
				String note_tag = rs.getString("note_tag");
				String location = rs.getString("location");
				int ifshare = rs.getInt("ifshare");
				int notet_id = rs.getInt("notet_id");
				jsonObject.put("notec_id", notec_id);
				jsonObject.put("note_title", note_title);
				jsonObject.put("note_body", note_body);
				jsonObject.put("createtime", createTime);
				jsonObject.put("updatetime", updateTime);
				jsonObject.put("note_tag", note_tag);
				jsonObject.put("location", location);
				jsonObject.put("ifshare", ifshare);
				jsonObject.put("notet_id", notet_id);
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

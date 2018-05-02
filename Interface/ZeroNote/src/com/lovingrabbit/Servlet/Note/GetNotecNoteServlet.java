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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetNotecNoteServlet extends HttpServlet{
	ResultSet rSet,rs;
	int user_id,ifshare;
	JSONObject jsonObject;
	JSONArray jsonArray;
	int note_id,count,notet_id;
	String note_title,note_body,createTime,updateTime,note_tag,location;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
				String usermobile = req.getParameter("mobile");
				String selectsId = "select id from user_infro where mobile="+ usermobile;
				String notec_id = req.getParameter("notec_id");
				System.out.println(selectsId);
				Untils untils = new Untils();
				count = 0;
				jsonArray = new JSONArray();
				try {
					rSet = untils.select(selectsId);
					while (rSet.next()) {
						user_id = rSet.getInt("id");
					}
					String selectNote = "select * from note where user_id=" + user_id +" and notec_id ="+notec_id;
					System.out.println(selectNote);
					rs = untils.select(selectNote);
					while (rs.next()) {
						note_id = rs.getInt("note_id");
						note_title = rs.getString("note_title");
						note_body = rs.getString("note_body");
						createTime = rs.getString("createtime");
						updateTime = rs.getString("updatetime");
						note_tag = rs.getString("note_tag");
						location = rs.getString("location");
						ifshare = rs.getInt("ifshare");
						notet_id = rs.getInt("notet_id");
						jsonObject = new JSONObject();
						jsonObject.put("note_id", note_id);
						jsonObject.put("note_title", note_title);
						jsonObject.put("note_body", note_body);
						jsonObject.put("createtime", createTime);
						jsonObject.put("updatetime", updateTime);
						jsonObject.put("note_tag", note_tag);
						jsonObject.put("location", location);
						jsonObject.put("ifshare", ifshare);
						jsonObject.put("notet_id", notet_id);
						jsonObject.put("count", count);
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
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
}

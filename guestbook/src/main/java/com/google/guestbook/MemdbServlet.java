package com.google.guestbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
  
public class MemdbServlet extends HttpServlet {
	
	private static Logger log = Logger.getLogger("Memdb");

	private HashMap<String, List<String>> entries = new HashMap<>();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("Get invoked");
		String guestbookName = req.getParameter("guestbookName");
		List<String> msgList = entries.get(guestbookName);
		if (msgList == null) {
		    msgList = new ArrayList<String>();
		    msgList.add("Test");
		    entries.put(guestbookName, msgList);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		resp.setContentType("application/json");
		mapper.writeValue(resp.getOutputStream(),msgList);
		resp.flushBuffer();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("Post invoked.");
	    String guestbookName = req.getParameter("guestbookName");
	    Key guestbookKey = KeyFactory.createKey("Guestbook", guestbookName);
	    String content = req.getParameter("content");
		List<String> msgList = entries.get(guestbookName);
		if (msgList == null) {
		    msgList = new ArrayList<String>();
		    entries.put(guestbookName, msgList);
		}
		msgList.add(content);
	    resp.sendRedirect("/guestbook.jsp?guestbookName=" + guestbookName);
	}

}

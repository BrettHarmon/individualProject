package net.codejava;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class getDataAjax
 * responsible for getting XML data 
 */
public class getDataAjax extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ajaxRequest(request,response);
	}
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ajaxRequest(request,response);
	}
	
	private void ajaxRequest(HttpServletRequest req,HttpServletResponse resp) {
		System.out.println("Data requested");
		String location = getLocation(req, "location");
		if(location == null){
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		File requested = new File("/WebContent" + location);
		resp.setContentType("application/xml");
		resp.setCharacterEncoding("UTF-8");
		//resp.getWriter().write(s);
	}
	
	private String getLocation(HttpServletRequest req, String name) {
		String val = req.getParameter(name);
		if (val == null) {
			return null;
		}
		try {
			return val;
		} catch (Exception e) {
			return null;
		}
	}
	 
}

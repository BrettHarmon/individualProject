package net.codejava;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class getDataAjax extends HttpServlet{
	private static final long serialVersionUID = 1L;
	 private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.

	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ajaxRequest(request,response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ajaxRequest(request,response);
	}
	
	private void ajaxRequest(HttpServletRequest req,HttpServletResponse resp) throws IOException {
		
		System.out.println("XML requested");
		String location = getLocation(req, "location");
		if(location == null){
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		File requested = new File("./WebContent" + location);

		if(!requested.exists()){
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write("File does not exist");
			return;
		}
		
		//FileInputStream fIS = new FileInputStream(requested);
		resp.setContentType("application/xml");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentLength((int) requested.length());
		
		BufferedInputStream input = null;
       BufferedOutputStream output = null;
       
		try {
           // Open streams.
           input = new BufferedInputStream(new FileInputStream(requested), DEFAULT_BUFFER_SIZE);
           output = new BufferedOutputStream(resp.getOutputStream(), DEFAULT_BUFFER_SIZE);

           // Write file contents to response.
           byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
           int length;
           while ((length = input.read(buffer)) > 0) {
               output.write(buffer, 0, length);
           }
       } finally {
           output.close();
           input.close();
       }
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

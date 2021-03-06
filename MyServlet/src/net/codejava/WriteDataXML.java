package net.codejava;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

/**
 * Servlet implementation class WriteDataXML
 * responsible for getting XML data 
 */
public class WriteDataXML extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			ajaxRequest(request,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			ajaxRequest(request,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void ajaxRequest(HttpServletRequest req,HttpServletResponse resp)  throws Exception{
		System.out.println("Write XML called");
		
		String d = getLocation(req, "day");
		String m = getLocation(req, "month");
		String y = getLocation(req, "year");
		String title = getLocation(req, "title");
		String desc = getLocation(req, "description");
		String location = getLocation(req, "location");
		
		if(d == null || m == null || y == null || location == null){
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		File requested = new File("./WebContent" + location);
		
		if(!requested.exists()){
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write("File does not exist");
			return;
		}
	
		
		//initialize DOM builders and parsers 
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(requested);
        Element root = document.getDocumentElement();
        
        //create base "task"
        Element task = document.createElement("task");
        root.appendChild(task);
        
        //Add date, title, and description to task
        
        Element year = document.createElement("year");
        year.appendChild(document.createTextNode(y));
        task.appendChild(year);
        
        Element month = document.createElement("month");
        month.appendChild(document.createTextNode(m));
        task.appendChild(month);
        
        Element day = document.createElement("day");
        day.appendChild(document.createTextNode(d));
        task.appendChild(day);
        
        Element titl = document.createElement("title");
        titl.appendChild(document.createTextNode(title));
        task.appendChild(titl);
        
        Element description = document.createElement("description");
        description.appendChild(document.createTextNode(desc));
        task.appendChild(description);

        //Add it to the root (body)
        root.appendChild(task);
        
        
    	DOMImplementationLS domImplementationLS = (DOMImplementationLS) document.getImplementation().getFeature("LS","3.0");
    	LSOutput lsOutput = domImplementationLS.createLSOutput();
    	FileOutputStream outputStream = new FileOutputStream(requested);
    	lsOutput.setByteStream((OutputStream) outputStream);
    	LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
    	lsSerializer.write(document, lsOutput);
    	//lsSerializer.setNewLine(" ");
    	outputStream.close();

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

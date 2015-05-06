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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

/**
 * Servlet implementation class RemoveXML
 * responsible for getting XML data 
 */
public class RemoveXML extends HttpServlet {
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
		System.out.println("XML node removed");
		
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
        //Create a node list to iterate through
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xpath.compile("task");
        Object exprResult = expr.evaluate(root, XPathConstants.NODESET);
        NodeList nodeList = (NodeList) exprResult;
        
        int nodeLocation = -1;
        for(int i = 0; i < nodeList.getLength(); i++){
        	 Element task = (Element) nodeList.item(i);
        	 String TaskYear = task.getChildNodes().item(0).getTextContent();
        	 String TaskMonth = task.getChildNodes().item(1).getTextContent();
        	 String TaskDay = task.getChildNodes().item(2).getTextContent();
        	 if(TaskYear.equals(y) && TaskMonth.equals(m) && TaskDay.equals(d)){
        		 String TaskTitle = task.getChildNodes().item(3).getTextContent();
            	 String TaskDesc = task.getChildNodes().item(4).getTextContent();
            	 if(TaskTitle.equals(title) & TaskDesc.equals(desc)){
            		 removeChilds(task);
            		 nodeLocation = i;            		 
            	 }
        	 }
        }
        if(nodeLocation != -1){
        	Element task = (Element) nodeList.item(nodeLocation);
        	task.getParentNode().removeChild(task);
        }
        
        
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
	public static void removeChilds(Node node) {
	    while (node.hasChildNodes()){
	        node.removeChild(node.getFirstChild());
	    }
	}
	 
}

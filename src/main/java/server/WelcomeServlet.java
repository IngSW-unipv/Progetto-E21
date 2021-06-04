package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rythmengine.Rythm;


public class WelcomeServlet extends HttpServlet {
 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		File f =  new File("src/main/resources/imgDB/img1.jpg");
        String encodstring = null;
		try {
			encodstring = encodeFileToBase64Binary(f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(encodstring);

 
		resp.getWriter().write(Rythm.render("helloworld.html", "Banana Joe", encodstring));
	}

	 private static String encodeFileToBase64Binary(File file) throws Exception{
	       FileInputStream fileInputStreamReader = new FileInputStream(file);
	       byte[] bytes = new byte[(int)file.length()];
	       fileInputStreamReader.read(bytes);
	       byte[] encodedBytes = Base64.getEncoder().encode(bytes);
	       return new String(encodedBytes);
	   }
	
	
	
	/*@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getPathInfo().equals("/save")) {
			Orders.add(req.getParameter("pizza"), req.getParameter("fullname"), req.getParameter("address"));			
		} 
		else if (req.getPathInfo().equals("/update")) {
			Orders.update(req.getParameter("id"), req.getParameter("pizza"), req.getParameter("fullname"), req.getParameter("address"));			
		} 
		else {
			Orders.delete(req.getParameter("id"));
		}
		resp.sendRedirect("/"); 
	}*/
}


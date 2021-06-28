package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rythmengine.Rythm;

import com.stevesoft.pat.apps.Message;

import auction.Auction;
import user.Participant;
import utilities.AuctionHouse;
import utilities.ChatMessage;


public class WelcomeServlet extends HttpServlet {
	
	private AuctionHouse auctionHouse = new AuctionHouse("Il mercationo della sirena");
 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getPathInfo().equals("/registerRequest")) {
			resp.getWriter().write(Rythm.render("register.html"));
		} else {
			
			/*ChatMessage msg1 = new ChatMessage("Marco","Percy", "Ciao" , "11:53");
			ChatMessage msg2 = new ChatMessage("Percy","Marco", "Ciao" , "11:54");
			ChatMessage msg3 = new ChatMessage("Marco","Percy", "Patata" , "11:55");
			ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();
			messages.add(msg1);
			messages.add(msg2);
			messages.add(msg3);
			resp.getWriter().write(Rythm.render("chat.html", 1 , "Marco", "Percy", messages));*/
			resp.getWriter().write(Rythm.render("login.html", ""));
		}
		
		

		/*File f =  new File("src/main/resources/imgDB/img1.jpg");
        String encodstring = null;
		try {
			encodstring = encodeFileToBase64Binary(f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(encodstring);

 
		resp.getWriter().write(Rythm.render("helloworld.html", "Banana Joe", encodstring)); */
	}
		
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		if (req.getPathInfo().equals("/login")) {
			int cookie = auctionHouse.login(req.getParameter("email"), req.getParameter("password"));			
			
			if (cookie == -1) {
				resp.getWriter().write(Rythm.render("login.html", "Username o password errati"));
			}
			else {
				
				resp.getWriter().write(Rythm.render("home.html", cookie, auctionHouse.getAuctions())); 
			}
		
		}
		else if (req.getPathInfo().equals("/register")) {
			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
			java.time.LocalDate birthday = java.time.LocalDate.parse(req.getParameter("bday"), formatter);
			Participant p1 = new Participant(req.getParameter("firstName"), req.getParameter("lastName"), req.getParameter("email"), req.getParameter("username"), req.getParameter("pwd"), birthday, req.getParameter("mobileNumber"));
			try {
				int cookie = auctionHouse.registerParticipantToDB(p1);
				File f =  new File("src/main/resources/imgDB/img1.jpg");
		        String encodstring = null;
				try {
					encodstring = encodeFileToBase64Binary(f);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        System.out.println(encodstring);
				resp.getWriter().write(Rythm.render("helloworld.html", auctionHouse.getParticipant(cookie).getUsername(), encodstring)); 
			
				 
			} catch (Exception e) {
				resp.getWriter().write(Rythm.render("register.html", "Errore durante la registrazione, riprova più tardi"));
			}
			
		}
		
		
		else if (req.getPathInfo().equals("/sendMessage")) {
			String message = req.getParameter("message");
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			String receiverUsername = req.getParameter("receiver");
			
			int ok = auctionHouse.saveMessage(cookie, receiverUsername, message);
			if (ok == -1)
			{
				resp.getWriter().write(Rythm.render("error.html", "Errore nell' invio del messaggio, riprova più tardi"));
			}
		}
		else if (req.getPathInfo().equals("/getMessage")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			String receiverUsername = req.getParameter("receiver");
			
			try {
				String messages[] =auctionHouse.getMessages(cookie, receiverUsername);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				resp.getWriter().write(Rythm.render("error.html", e.getMessage()));
			}
			
			
		}
		                                                
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


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
		}
		else if (req.getPathInfo().equals("/productDetails")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			resp.getWriter().write(Rythm.render("productDetails.html", cookie, auctionHouse.getAuction(req.getParameter("auctioner"), req.getParameter("auctionID"))));
		}
		else {
			resp.getWriter().write(Rythm.render("login.html", ""));
		}
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
			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
			java.time.LocalDate birthday = java.time.LocalDate.parse(req.getParameter("bday"), formatter);
			Participant p1 = new Participant(req.getParameter("firstName"), req.getParameter("lastName"), req.getParameter("email"), req.getParameter("username"), req.getParameter("pwd"), birthday, req.getParameter("mobileNumber"));
			try {
				int cookie = auctionHouse.registerParticipantToDB(p1);
				resp.getWriter().write(Rythm.render("home.html", cookie, auctionHouse.getAuctions())); 		 
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
		else if (req.getPathInfo().equals("/productDetails")) {
			
		}
		
		                                                
	}

	 private static String encodeFileToBase64Binary(File file) throws Exception{
	       FileInputStream fileInputStreamReader = new FileInputStream(file);
	       byte[] bytes = new byte[(int)file.length()];
	       fileInputStreamReader.read(bytes);
	       byte[] encodedBytes = Base64.getEncoder().encode(bytes);
	       return new String(encodedBytes);
	   }
}


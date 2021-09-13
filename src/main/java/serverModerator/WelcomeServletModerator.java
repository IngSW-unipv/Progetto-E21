package serverModerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.eclipse.jetty.server.Request;
import org.rythmengine.Rythm;

import com.stevesoft.pat.apps.Message;

import auction.Auction;
import auction.Item;
import auction.Lot;
import user.Participant;
import utilities.AuctionHouse;
import utilities.ChatMessage;


public class WelcomeServletModerator extends HttpServlet {
	
	private AuctionHouse auctionHouse = new AuctionHouse("Il mercationo della sirena");
	private ArrayList<Auction> pendingAucton = new ArrayList<Auction>();
 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getPathInfo().equals("/productDetails")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			try {
				resp.getWriter().write(Rythm.render("productDetailsModerator.html", cookie, new Auction(req.getParameter("auctionID")), new Participant(req.getParameter("auctioner")).getImg(), ""));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		else if (req.getPathInfo().equals("/home")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			try {
				resp.getWriter().write(Rythm.render("homeModerator.html", cookie, auctionHouse.getPendingAuctions()));
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		else if (req.getPathInfo().equals("/approveAuction")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			try {
				auctionHouse.approveAuction(req.getParameter("auctionID"));
				resp.getWriter().write(Rythm.render("homeModerator.html", cookie, auctionHouse.getPendingAuctions()));
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		else if (req.getPathInfo().equals("/rejectAuction")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			try {
				auctionHouse.rejectAuction(req.getParameter("auctionID"));
				resp.getWriter().write(Rythm.render("homeModerator.html", cookie, auctionHouse.getPendingAuctions()));
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
		else {								
			resp.getWriter().write(Rythm.render("loginModerator.html", ""));			//Gestione richiesta pagina di login e default
		}	
	}
		
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		if (req.getPathInfo().equals("/login")) {
			try {
				int cookie = auctionHouse.loginModerator(req.getParameter("username"), req.getParameter("password"));			
				
				if (cookie == -1) {
					resp.getWriter().write(Rythm.render("loginModerator.html", "Username o password errati"));
				}
				else {
					
					resp.getWriter().write(Rythm.render("homeModerator.html", cookie, auctionHouse.getPendingAuctions())); 
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
		


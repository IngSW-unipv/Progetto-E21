package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rythmengine.Rythm;

import com.stevesoft.pat.apps.Message;

import auction.Auction;
import auction.Item;
import auction.Lot;
import user.Participant;
import utilities.AuctionHouse;
import utilities.ChatMessage;


public class WelcomeServlet extends HttpServlet {
	
	private AuctionHouse auctionHouse = new AuctionHouse("Il mercationo della sirena");
	private ArrayList<Auction> pendingAucton = new ArrayList<Auction>();
 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getPathInfo().equals("/registerRequest")) {
			resp.getWriter().write(Rythm.render("register.html"));	
		}
		else if (req.getPathInfo().equals("/productDetails")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			try {
				resp.getWriter().write(Rythm.render("productDetails.html", cookie, auctionHouse.getAuction(req.getParameter("auctioner"), req.getParameter("auctionID")), auctionHouse.getProfile(req.getParameter("auctioner")).getImg()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (req.getPathInfo().equals("/createAuction")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			resp.getWriter().write(Rythm.render("createAuction.html", cookie));
		}
		else if (req.getPathInfo().equals("/home")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			try {
				resp.getWriter().write(Rythm.render("home.html", cookie, auctionHouse.getAuctions()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		else if (req.getPathInfo().equals("/visitProfile")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			try {
				resp.getWriter().write(Rythm.render("profile.html", cookie, auctionHouse.getProfile(req.getParameter("profile")), auctionHouse.getReviews(req.getParameter("profile"))));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		else if (req.getPathInfo().equals("/editProfile")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			resp.getWriter().write(Rythm.render("editProfile.html", cookie, "", "", "", ""));
		}
		else if (req.getPathInfo().equals("/myProfile")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			String pr = auctionHouse.getUsername(cookie);
			try {
				resp.getWriter().write(Rythm.render("myProfile.html", cookie, auctionHouse.getProfile(pr), auctionHouse.getReviews(pr)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		else {
			resp.getWriter().write(Rythm.render("login.html", ""));
		}
		
	}
		
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		if (req.getPathInfo().equals("/login")) {
			try {
				int cookie = auctionHouse.login(req.getParameter("email"), req.getParameter("password"));			
				
				if (cookie == -1) {
					resp.getWriter().write(Rythm.render("login.html", "Username o password errati"));
				}
				else {
					
					resp.getWriter().write(Rythm.render("home.html", cookie, auctionHouse.getAuctions())); 
				}
			}catch (Exception e) {
				e.printStackTrace();
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
			
			int ok;
			try {
				ok = auctionHouse.saveMessage(cookie, receiverUsername, message);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (req.getPathInfo().equals("/saveAddress")) {
			int cookie = Integer.parseInt(req.getCookies()[0].getValue());
			try {
				auctionHouse.updateAddress(cookie, req.getParameter("country"), req.getParameter("city"), req.getParameter("road"), req.getParameter("number"), req.getParameter("cap"));
				resp.getWriter().write(Rythm.render("editProfile.html", cookie, "", "", "Updated", "" ));
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				resp.getWriter().write(Rythm.render("editProfile.html", cookie, "", "", e.getMessage(), "" ));
			}	
		}
		
		else if (req.getPathInfo().equals("/saveIntro")) {
			int cookie = Integer.parseInt(req.getCookies()[0].getValue());
			try {
				auctionHouse.updateIntro(cookie, req.getParameter("intro"));
				resp.getWriter().write(Rythm.render("editProfile.html", cookie, "Updated", "", "", "" ));
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				resp.getWriter().write(Rythm.render("editProfile.html", cookie, e.getMessage(), "", "", "" ));
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
		
		//Qui vengono gestiti i parametri ricevuti durante la prima fase della creazione di un asta e viene reindirizzato l'utente alla pagina di creazione del primo lotto
		else if (req.getPathInfo().equals("/createAuction")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
			java.time.LocalDate sDate = java.time.LocalDate.parse(req.getParameter("sDate"), formatter);
			java.time.LocalDate eDate = java.time.LocalDate.parse(req.getParameter("eDate"), formatter);
			try {
				Auction pAuction = new Auction(req.getParameter("name"), auctionHouse.getUsername(cookie), sDate, eDate, Double.parseDouble(req.getParameter("sPrice")), Double.parseDouble(req.getParameter("rise")), cookie);
				pendingAucton.add(pAuction);
				resp.getWriter().write(Rythm.render("newLot.html", cookie));
			}
			catch (Exception e) {
				resp.getWriter().write(Rythm.render("error.html", e.getMessage()));
			}	
		}
		
		//Qui vengono gestiti i parametri ricevuti durante la fase di creazione del lotto e viene reindirizzato l'utente alla pagina di creazione del primo oggetto
		else if (req.getPathInfo().equals("/addLot")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			try {
				for (int i=0; i< pendingAucton.size(); i++)
				{
					if( pendingAucton.get(i).getId() == cookie) {
						Lot pLot = new Lot(req.getParameter("name"), req.getParameter("descrtiption"), pendingAucton.get(i).getLots().size());
						pendingAucton.get(i).addLot(pLot);	
					}
				}
				
				resp.getWriter().write(Rythm.render("newItem.html", cookie));
			}
			catch (Exception e) {
				resp.getWriter().write(Rythm.render("error.html", e.getMessage()));
			}	
		}
		
		//Qui vengono gestiti i parametri ricevuti durante la fase di inserimento di un oggetto e viene reindirizzato l'utente alla pagina di inserimento di un'altro oggetto
				else if (req.getPathInfo().equals("/addItem")) {
					int cookie = Integer.parseInt(req.getParameter("cookie"));
					try {
		
						for (int i=0; i< pendingAucton.size(); i++)
						{
							if( pendingAucton.get(i).getId() == cookie) {
										Item pItem = new Item(req.getParameter("name"), req.getParameter("descrtiption"), req.getParameter("image"), pendingAucton.get(i).getLots().get(pendingAucton.get(i).getLots().size()).getItems().size());
										pendingAucton.get(i).getLots().get(pendingAucton.get(i).getLots().size()).getItems().add(pItem);
							}
						}
						resp.getWriter().write(Rythm.render("newItem.html", cookie));
					}
					catch (Exception e) {
						resp.getWriter().write(Rythm.render("error.html", e.getMessage()));
					}	
				}
		
		
		
		                                                
	}
	//metodo per la conversione in base64 di un immagine
	 private static String encodeFileToBase64Binary(File file) throws Exception{
	       FileInputStream fileInputStreamReader = new FileInputStream(file);
	       byte[] bytes = new byte[(int)file.length()];
	       fileInputStreamReader.read(bytes);
	       byte[] encodedBytes = Base64.getEncoder().encode(bytes);
	       return new String(encodedBytes);
	   }
}


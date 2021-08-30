package server;

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

@WebServlet("/upload")
@MultipartConfig
public class WelcomeServlet extends HttpServlet {
	
	private static final MultipartConfigElement MULTI_PART_CONFIG = new MultipartConfigElement("./tmp");
	
	private AuctionHouse auctionHouse = new AuctionHouse("Il mercationo della sirena");
	private ArrayList<Auction> pendingAucton = new ArrayList<Auction>();
 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getPathInfo().equals("/registerRequest")) {					//Gestione richiesta pagina di registrazione
			resp.getWriter().write(Rythm.render("register.html"));	
		}
		else if (req.getPathInfo().equals("/productDetails")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			try {
				resp.getWriter().write(Rythm.render("productDetails.html", cookie, auctionHouse.getAuction(req.getParameter("auctionID")), auctionHouse.getProfile(req.getParameter("auctioner")).getImg(), ""));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if (req.getPathInfo().equals("/createAuction")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			resp.getWriter().write(Rythm.render("createAuction.html", cookie));
		}
		else if (req.getPathInfo().equals("/placeBid")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			try {
				String msg = auctionHouse.placeBid(auctionHouse.getUsername(cookie), req.getParameter("auctionID"));
				resp.getWriter().write(Rythm.render("productDetails.html", cookie, auctionHouse.getAuction(req.getParameter("auctionID")), auctionHouse.getProfile(req.getParameter("auctioner")).getImg(), msg));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if (req.getPathInfo().equals("/getPayments")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			try {
				String un = auctionHouse.getUsername(cookie);
				resp.getWriter().write(Rythm.render("payments.html", cookie, auctionHouse.getClosedAuctions(cookie), auctionHouse.getUsername(cookie)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if (req.getPathInfo().equals("/home")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			try {
				resp.getWriter().write(Rythm.render("home.html", cookie, auctionHouse.getOpenAuctions()));
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		else if (req.getPathInfo().equals("/visitProfile")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			try {
				resp.getWriter().write(Rythm.render("profile.html", cookie, auctionHouse.getProfile(req.getParameter("profile")), auctionHouse.getReviews(req.getParameter("profile"))));
			} catch (Exception e) {
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
				e.printStackTrace();
			} 
		}
		else if (req.getPathInfo().equals("/checkPayment")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			try {
				resp.getWriter().write(Rythm.render("checkPayment.html", cookie, auctionHouse.getAuction(req.getParameter("auctionID")), auctionHouse.getUsername(cookie), ""));
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		//DA COMPLETARE NON FUNZIONA ANCORA 
		else if (req.getPathInfo().equals("/chatList")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			try {
				resp.getWriter().write(Rythm.render("chatList.html", cookie, auctionHouse.getMessageList(cookie)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		else if (req.getPathInfo().equals("/paymentUtil")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			try {
				int operation = auctionHouse.paymentNextStep(cookie, req.getParameter("auctionID"));
				if (operation == 0){
					resp.getWriter().write(Rythm.render("checkPayment.html", cookie, auctionHouse.getAuction(req.getParameter("auctionID")), auctionHouse.getUsername(cookie), ""));
				}
				else if (operation == 1){
					resp.getWriter().write(Rythm.render("writeReview.html", cookie, auctionHouse.getAuction(req.getParameter("auctionID"))));
				}
				else
				{
					resp.getWriter().write(Rythm.render("checkPayment.html", cookie, auctionHouse.getAuction(req.getParameter("auctionID")), auctionHouse.getUsername(cookie), "Error connecting to database, please try again later"));
				}
				
			} catch (Exception e) {
				resp.getWriter().write(Rythm.render("error.html", cookie, e.getMessage()));
				e.printStackTrace();
			} 
		}
		else {								
			resp.getWriter().write(Rythm.render("login.html", ""));			//Gestione richiesta pagina di login e default
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
					
					resp.getWriter().write(Rythm.render("home.html", cookie, auctionHouse.getOpenAuctions())); 
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
				resp.getWriter().write(Rythm.render("home.html", cookie, auctionHouse.getOpenAuctions())); 		 
			} catch (Exception e) {
				resp.getWriter().write(Rythm.render("register.html", "Errore durante la registrazione, riprova pi� tardi"));
			}
			
		}
		//SEND MESSAGE DA FINIRE
		else if (req.getPathInfo().equals("/sendMessage")) {
			int cookie = Integer.parseInt(req.getCookies()[0].getValue());
			String receiverUsername = req.getParameter("profile");
			String text = req.getParameter("message");
			try {
				auctionHouse.saveMessage(cookie, receiverUsername, text);
				resp.getWriter().write(Rythm.render("profile.html", cookie, auctionHouse.getProfile(req.getParameter("profile")), auctionHouse.getReviews(req.getParameter("profile"))));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				resp.getWriter().write(Rythm.render("error.html", cookie, e.getMessage()));
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
		
		//metodo per aggiornamento immagine profilo
		else if (req.getPathInfo().equals("/saveImg")) {
			int cookie = Integer.parseInt(req.getCookies()[0].getValue());
			try {
				req.setAttribute(Request.MULTIPART_CONFIG_ELEMENT, MULTI_PART_CONFIG);	
				Part filePart = req.getPart("img"); // Retrieves <input type="file" name="file">
			    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
			    InputStream fileContent = filePart.getInputStream();
				auctionHouse.updateImg(cookie, fileContent);
				resp.getWriter().write(Rythm.render("editProfile.html", cookie, "", "", "", "Updated" ));
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				resp.getWriter().write(Rythm.render("editProfile.html", cookie, "", "", "", e.getMessage() ));
			}	
		}
		
		//metodo per aggiornamento carta di credito
		else if (req.getPathInfo().equals("/saveCard")) {
			int cookie = Integer.parseInt(req.getCookies()[0].getValue());
			try {
				auctionHouse.updateCard(cookie, req.getParameter("fName"), req.getParameter("lName"), req.getParameter("date"), req.getParameter("number"), req.getParameter("cvv"));
				resp.getWriter().write(Rythm.render("editProfile.html", cookie, "", "Updated", "", "" ));
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				resp.getWriter().write(Rythm.render("editProfile.html", cookie, "", e.getMessage(), "", "" ));
			}	
		}
		
		
		else if (req.getPathInfo().equals("/getMessage")) {
			int cookie = Integer.parseInt(req.getParameter("cookie"));
			String receiverUsername = req.getParameter("receiver");
			
			try {
				String messages[] =auctionHouse.getMessages(cookie, receiverUsername);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				resp.getWriter().write(Rythm.render("error.html", cookie, e.getMessage()));
			}	
		}
		
		//Qui vengono gestiti i parametri ricevuti durante la prima fase della creazione di un asta e viene reindirizzato l'utente alla pagina di creazione del primo lotto
		else if (req.getPathInfo().equals("/createAuction")) {
			int cookie = Integer.parseInt(req.getCookies()[0].getValue());
			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime sDate = java.time.LocalDateTime.parse(req.getParameter("sDate") +" "+ req.getParameter("sTime") , formatter);
			LocalDateTime eDate = java.time.LocalDateTime.parse(req.getParameter("eDate") +" "+ req.getParameter("eTime"), formatter);
			try {
				Boolean check = ((req.getParameter("timeExtension") == null) ? false : true);
				Auction pAuction = new Auction(req.getParameter("name"), auctionHouse.getUsername(cookie), sDate, eDate, Double.parseDouble(req.getParameter("sPrice")), Double.parseDouble(req.getParameter("rise")), cookie, check);
				pendingAucton.add(pAuction);
				resp.getWriter().write(Rythm.render("newLot.html", cookie));
			}
			catch (Exception e) {
					resp.getWriter().write(Rythm.render("error.html", cookie, e.getMessage()));
				}	
		}
		
		//Qui vengono gestiti i parametri ricevuti durante la fase di creazione del lotto e viene reindirizzato l'utente alla pagina di creazione del primo oggetto
		else if (req.getPathInfo().equals("/addLot")) {
			int cookie = Integer.parseInt(req.getCookies()[0].getValue());
			try {
				for (int i=0; i< pendingAucton.size(); i++)
				{
					if( pendingAucton.get(i).getId() == cookie) {
						Lot pLot = new Lot(req.getParameter("name"), req.getParameter("description"), pendingAucton.get(i).getLots().size());
						pendingAucton.get(i).addLot(pLot);	
					}
				}
				
				resp.getWriter().write(Rythm.render("newItem.html", cookie));
			}
			catch (Exception e) {
				resp.getWriter().write(Rythm.render("error.html", cookie, e.getMessage()));
			}	
		}
		
		//Qui vengono gestiti i parametri ricevuti durante la fase di inserimento di un oggetto e viene reindirizzato l'utente alla pagina di inserimento di un'altro lotto
		else if (req.getPathInfo().equals("/newLot")) {
			int cookie = Integer.parseInt(req.getCookies()[0].getValue());
			try {

				for (int i=0; i< pendingAucton.size(); i++)
				{
					if( pendingAucton.get(i).getId() == cookie) {
						req.setAttribute(Request.MULTIPART_CONFIG_ELEMENT, MULTI_PART_CONFIG);	
						Part filePart = req.getPart("img"); // Retrieves <input type="file" name="file">
					    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
					    InputStream fileContent = filePart.getInputStream();	
						Item pItem = new Item(req.getParameter("name"), req.getParameter("description"), fileName, fileContent);
						pendingAucton.get(i).getLots().get(pendingAucton.get(i).getLots().size()-1).getItems().add(pItem);
					}
				}
				resp.getWriter().write(Rythm.render("newLot.html", cookie));
			}
			catch (Exception e) {
				resp.getWriter().write(Rythm.render("error.html", cookie, e.getMessage()));
			}	
		}
		
		//Qui vengono gestiti i parametri ricevuti durante la fase di inserimento di un oggetto e viene reindirizzato l'utente alla pagina di inserimento di un'altro oggetto
				else if (req.getPathInfo().equals("/addItem")) {
					int cookie = Integer.parseInt(req.getCookies()[0].getValue());
					try {
		
						for (int i=0; i< pendingAucton.size(); i++)
						{
							if( pendingAucton.get(i).getId() == cookie) {
								req.setAttribute(Request.MULTIPART_CONFIG_ELEMENT, MULTI_PART_CONFIG);		
								Part filePart = req.getPart("img"); // Retrieves <input type="file" name="file">
							    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
							    InputStream fileContent = filePart.getInputStream();	
								Item pItem = new Item(req.getParameter("name"), req.getParameter("description"), fileName, fileContent);
								pendingAucton.get(i).getLots().get(pendingAucton.get(i).getLots().size()-1).getItems().add(pItem);
							}
						}
						resp.getWriter().write(Rythm.render("newItem.html", cookie));
					}
					catch (Exception e) {
						resp.getWriter().write(Rythm.render("error.html", cookie, e.getMessage()));
					}	
				}
		
		//Qui vengono gestiti i parametri ricevuti durante la fase di inserimento di un oggetto e creata l'asta
				else if (req.getPathInfo().equals("/confirmAuction")) {
					int cookie = Integer.parseInt(req.getCookies()[0].getValue());
					try {
		
						for (int i=0; i< pendingAucton.size(); i++)
						{
							if( pendingAucton.get(i).getId() == cookie) {
								req.setAttribute(Request.MULTIPART_CONFIG_ELEMENT, MULTI_PART_CONFIG);	
								Part filePart = req.getPart("img"); // Retrieves <input type="file" name="file">
							    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
							    InputStream fileContent = filePart.getInputStream();	
								Item pItem = new Item(req.getParameter("name"), req.getParameter("description"), fileName, fileContent);
								pendingAucton.get(i).getLots().get(pendingAucton.get(i).getLots().size()-1).getItems().add(pItem);
								auctionHouse.registerAuctionToDB(auctionHouse.getUsername(cookie), pendingAucton.get(i));
								resp.getWriter().write(Rythm.render("productDetails.html", cookie, pendingAucton.get(i), auctionHouse.getProfile(auctionHouse.getUsername(cookie)).getImg()));
								pendingAucton.remove(i);
							}
						}
					}
					catch (Exception e) {
						System.out.println(e.getMessage());
						resp.getWriter().write(Rythm.render("error.html", cookie, e.getMessage()));
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


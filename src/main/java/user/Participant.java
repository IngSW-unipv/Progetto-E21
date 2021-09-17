package user;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;

import user.userDetails.Address;

/**
 * This class describes a Participant and all its details
 */
public class Participant {
	
	    private String firstName, lastName, email, username, mobileNumber, password, img, intro;
	    LocalDate bDay;
	    Address address;
	    
	    public Participant(String firstName, String lastName, String email, String username, String password, LocalDate birthday, String mobileNumber) {
			
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.username = username;
			this.mobileNumber = mobileNumber;
			this.password = password;
			this.bDay = birthday;
			
		}
	    
	    public Participant(String firstName, String lastName, String email, String username, String password, LocalDate birthday, String mobileNumber, String img, String intro) {
			
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.username = username;
			this.mobileNumber = mobileNumber;
			this.password = password;
			this.bDay = birthday;
			this.img = img;
			this.intro = intro;
		}
	    
	    
	    public Participant(String firstName, String lastName, String email, String username, String password, Address address, LocalDate birthday, String mobileNumber, String img, String intro) {
			
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.username = username;
			this.mobileNumber = mobileNumber;
			this.password = password;
			this.bDay = birthday;
			this.address = address;
			this.img = img;
			this.intro = intro;
		}

	/**
	 * Outputs a Participant inserted into DB given his username
	 * @param username Username of the Participant
	 * @throws SQLException
	 */
	public Participant(String username) throws SQLException
	    {
	    	Connection cn = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net:3306/sql11421731", "sql11421731", "83bkPjI9Yf");
			
			Statement st;
			ResultSet rs;
			String sql;
			Participant p1;
			   try {
		           sql = "SELECT * FROM participant where username = '" + username + "';";
				   st = cn.createStatement(); //creo  uno statement sulla coneesione
				   rs = st.executeQuery(sql); //faccio la query su uno statement
				   while(rs.next() == true) {
					   File f =  new File("src/main/resources/imgDB/profilePics/" + rs.getString("img") + ".jpg");
					   String encodstring = null;
					   try {
							encodstring = encodeFileToBase64Binary(f);
					   } catch (Exception e) {
						    e.printStackTrace();
					   }
					   this.firstName = rs.getString("firstName");
					   this.lastName = rs.getString("lastName");
					   this.email = rs.getString("email");
					   this.username = username;
					   this.password = rs.getString("password");
					   this.bDay = rs.getDate("birthday").toLocalDate();
					   this.intro = rs.getString("intro");
					   this.mobileNumber = rs.getString("mobileNumber");
					   this.img = encodstring;
				  }
			   } catch(SQLException e) {
				   System.out.println("errore: " + e.getMessage());
			   }
			   finally {
				   cn.close();
			   } 
	    	
	    	
	    }

		/**
		 * This method encodes images into base64
		 * @param file Image file passed to encode
		 * @return Returns the encoded bytes of the image
		 * @throws Exception
		 */
		private static String encodeFileToBase64Binary(File file) throws Exception{
		        FileInputStream fileInputStreamReader = new FileInputStream(file);
		        byte[] bytes = new byte[(int)file.length()];
				fileInputStreamReader.read(bytes);
				byte[] encodedBytes = Base64.getEncoder().encode(bytes);
				return new String(encodedBytes);
		}
	    
	    
	    public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public String getEmail() {
			return email;
		}

		public String getUsername() {
			return username;
		}

		public String getMobileNumber() {
			return mobileNumber;
		}

		public String getPassword() {
			return password;
		}
		
		public LocalDate getBirthday() {
			return bDay;
		}
		
		public Address getAddress() {
			return address;
		}
		
		
		public String getImg() {
			return img;
		}

		public String getIntro() {
			return intro;
		}

		public LocalDate getbDay() {
			return bDay;
		}
		
			   
}
	



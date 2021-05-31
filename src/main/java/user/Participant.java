package user;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

public class Participant {
	
	 private String firstName;
	    private String lastName;
	    private String email;
	    private String username;
	    private int mobileNumber;
	    private String password;
	    private String address;
	    Date birthday;
	    
	    
	    public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public int getMobileNumber() {
			return mobileNumber;
		}
		public void setMobileNumber(int mobileNumber) {
			this.mobileNumber = mobileNumber;
		}
		public String getPassword() {
			return password;
		}
		public void setPasswrd(String password) {
			this.password = password;
		}
		
		public Date getBirthday() {
			return birthday;
		}
		public void setBirthday(Date birthday) {
			this.birthday = birthday;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		
		public Participant(String firstName, String lastName, String email, String username, String password, String address, int mobileNumber, Date birthday) {
			
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.username = username;
			this.mobileNumber = mobileNumber;
			this.password = password;
			this.birthday = birthday;
			
			
			
		}
		
		public Participant() {
			// TODO Auto-generated constructor stub
		}
		//aggiorna
		public static ArrayList<Participant> listOfParticipant() throws SQLException {
			Connection cn = null;
			Statement st;
			ResultSet rs;
			String sql;
			//___________connesione___________
			   try {
				 
				cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/registration_system", "root", "Crisele05");//Establishing connection
				System.out.println("Connected With the database successfully");	
			} catch (SQLException e) {
				
				System.out.println("Error while connecting to the database");
			
					}
		
			   ArrayList<Participant> list = new ArrayList<Participant>();
		        sql = "SELECT * FROM participant;";
			   //____________query___________
			   try {
				   st = cn.createStatement(); //creo sempre uno statement sulla coneesione
				   
				   rs = st.executeQuery(sql); //faccio la query su uno statement
				   while(rs.next() == true) {
					   
					   Participant p1 = new Participant(rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("address"), rs.getInt("mobile_number"), rs.getDate("birthday"));
					   
					   System.out.println(rs.getNString(1) + "\t" + rs.getNString("last_name") + rs.getDate("birthday"));
			
				   }
				   
			   } catch(SQLException e) {
				   System.out.println("errore: " + e.getMessage());
			   } //fine try-catch  
			return list;
		}
		
		public static void deleteParticipant(Participant p1) throws SQLException {
			
			
			Connection cn = null;
			Statement st;
			ResultSet rs;
			String sql;
			
			//___________connesione___________
			   try {
				 
				cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/registration_system", "root", "Crisele05");//Establishing connection
				System.out.println("Connected With the database successfully");	
			} catch (SQLException e) {
				
				System.out.println("Error while connecting to the database");
			
					}
			   String username = p1.getUsername();
		
		
		        sql = "DELETE FROM PARTICIPANT WHERE USERNAME= " + username;
			   //____________query___________
			   try {
				   st = cn.createStatement(); //creo sempre uno statement sulla coneesione
				   
				   rs = st.executeQuery(sql); //faccio la query su uno statement
				   while(rs.next() == true) {
					   
					  
					   
					   System.out.println(rs.getNString(1) + "\t" + rs.getNString("username") );
			
				   }
				   
			   } catch(SQLException e) {
				   System.out.println("errore: " + e.getMessage());
			   } //fine try-catch  
			   
			   
			   //ha dei metodi per leggere/scrivere e modificare il db registration_system
			   
			   
			   cn.close();
			   System.out.println("connection terminated"); 
			
		}
		
		public void newParticipant(Participant p1) throws SQLException {
			
			Connection cn = null;
			Statement st;
			ResultSet rs;
			String sql;
			
		
			   try {
				 
				cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/registration_system", "root", "Crisele05");//Establishing connection
				System.out.println("Connected With the database successfully");	
			} catch (SQLException e) {
				
				System.out.println("Error while connecting to the database");
			
					}
			   
			   
			   String first_name = p1.getFirstName();
			   String last_name = p1.getLastName();
			   String username = p1.getUsername();
			   String password = p1.getPassword();
			   String email = p1.getEmail();
			   String address = p1.getPassword();
			   int mobile_number = p1.getMobileNumber();
			   Calendar birthday = Calendar.getInstance();
			   birthday.setTime(p1.getBirthday());
			   String sqlData = birthday.get(Calendar.YEAR) + "-" + birthday.get(Calendar.MONTH) + "-" + birthday.get(Calendar.DAY_OF_MONTH);
			   
		
		       
			   
			     try {
			    	  // Statements allow to issue SQL queries to the database
			            st = cn.createStatement();
			            // Result set get the result of the SQL query
			            rs = st.executeQuery("select * from participant");
			                  
			            writeResultSet(rs);

			            // PreparedStatements can use variables and are more efficient
			            PreparedStatement preparedStatement = cn .prepareStatement("insert into participant ( ?, ?, ?, ? , ?, ?, ?, ? )");
			               
			            // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
			            // Parameters start with 1
			            preparedStatement.setString(1, p1.getFirstName());
			            preparedStatement.setString(2, p1.getLastName());
			            preparedStatement.setString(3, p1.getUsername());
			            preparedStatement.setDate(6, new java.sql.Date(2009, 12, 11));
			            preparedStatement.setString(5, p1.getEmail());
			            preparedStatement.setString(4, p1.getPassword());
			            preparedStatement.setInt(8, p1.getMobileNumber());
			            preparedStatement.setString(7, p1.getAddress());
			            preparedStatement.executeUpdate();
				  
			   } catch(SQLException e) {
				   System.out.println("errore: " + e.getMessage());
			   } //fine try-catch  
			   
			   
			   //ha dei metodi per leggere/scrivere e modificare il db registration_system
			   
			   
			   cn.close();
			   System.out.println("connection terminated"); 
		}
		
		
		
		private void writeResultSet(ResultSet rs) {
			// TODO Auto-generated method stub
			
		}
		public static void main(String[] args) throws SQLException {
			
			Participant p1 = new Participant ();
			p1.setFirstName("crisele");
			p1.setLastName("ARIOLA");
			p1.setUsername("crisele05");
			p1.setPassword("password");
			p1.setEmail("crisele05@gmail.com");
			p1.setMobileNumber(380);
			p1.setAddress("via Rismondo 72");
		
			p1.setBirthday(java.sql.Date.valueOf("2013-09-04"));
			
			
			
			
			System.out.println(p1.getBirthday());
			
			
			p1.newParticipant(p1);
		}
			   
}
	



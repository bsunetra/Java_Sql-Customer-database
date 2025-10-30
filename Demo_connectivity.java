import java.sql.*;
public class demo{
	public static void main(String[] args) {
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/billing_details","root","root");
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("Select * from cust_details");
			while(rs.next()) {
				System.out.println(rs.getString(1)+"\t"+rs.getString(2));
				
			}
			
		}
		catch(Exception e) {
			System.out.println(e.toString());
			
		}
	}
}

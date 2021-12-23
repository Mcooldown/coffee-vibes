package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public final class Connect {
	
	private ResultSet rs;
	private static Connection con;
	private static Statement stat;
	private static Connect connect;
	
	public static Connect getConnection() {
		
		if(connect == null) {
			connect = new Connect();
		}
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/coffeevibes_group2_bd01", "root", "");
			stat = con.createStatement();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return connect;
	}
	
	public ResultSet executeQuery(String query) {
		try {
			rs = stat.executeQuery(query);
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public PreparedStatement prepareStatement(String query) {
		try {
			return con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

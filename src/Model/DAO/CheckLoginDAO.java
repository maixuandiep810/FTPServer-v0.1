package Model.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.BEAN.Account;
import Util.DBConnection;

public class CheckLoginDAO {
	/*
	 * 
	 */
	public boolean isValidUsername(String username) {
		boolean check = false;
		String myQuery = "SELECT * FROM account WHERE username='"+username+"'";	
		try {
			Connection conn = DBConnection.getConn();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(myQuery);
			if (rs.next()) {
				check = true;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return check;
	}
	
	public boolean isValidPassword(String username, String password) {
		boolean check = false;
		String myQuery = "SELECT * FROM account WHERE username='"+username+"' "
				+ "AND password='"+password+"'";
		try {
			Connection conn = DBConnection.getConn();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(myQuery);
			if (rs.next()) {
				check = true;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return check;
	}
	
	public Account getAccount(String username) {
		Account acc = new Account();
		String myQuery = "SELECT * FROM account WHERE username='"+username+"'";	
		try {
			Connection conn = DBConnection.getConn();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(myQuery);
			if (rs.next()) {
				acc.setUsername(rs.getObject("username").toString());
				acc.setLogged(true);
				return acc;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}

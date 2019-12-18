package Model.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Util.DBConnection;

public class UserPathDAO {
	/*
	 * 
	 */
	public String getPath(String username) {
		String basePath = "";
		String myQuery = "SELECT disk.disk_path, folder.folder_path FROM folder JOIN disk ON folder.disk_id=disk.disk_id WHERE account_id=(SELECT account_id FROM account WHERE username='"+username+"')";
		try {
			Connection conn = DBConnection.getConn();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(myQuery);
			if (rs.next()) {
				basePath = rs.getObject(1) + "\\" + rs.getObject(2);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return basePath;
	}
}

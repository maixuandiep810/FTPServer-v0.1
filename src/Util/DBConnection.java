package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
	
	private static final String _DriverName = "com.mysql.jdbc.Driver";
	private static final String _Url = "jdbc:mysql://localhost:3306/ftp_server";
	private static final String _Username = "root";
	private static final String _Password = "";
	/**
	 * 
	 */
	public DBConnection() {
	}
	
	public static Connection getConn() throws ClassNotFoundException, SQLException {
		Class.forName(_DriverName);
		Connection conn = DriverManager.getConnection(_Url, _Username, _Password);
		return conn;
	}
}

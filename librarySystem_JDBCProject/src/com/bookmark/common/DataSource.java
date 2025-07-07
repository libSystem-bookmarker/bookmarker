package com.bookmark.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DataSource {
	
	//Scanner 설정
	public static Scanner sc = new Scanner(System.in);
	
	//공유 db 설정
	private static String driverClassName = "oracle.driver.OracleDriver";
	private static String url = "jdbc:oracle:thin:@localhost:1522:xe";
	private static String username = "hr";
	private static String password = "hr";

	// class load 시 딱 한 번 사용되는 static initializer
	static {
		try {
			Class.forName(driverClassName);
			System.out.println("driver load");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// connection
	public Connection getConnection() {
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return con;
	}

	// connection close
	public void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

}

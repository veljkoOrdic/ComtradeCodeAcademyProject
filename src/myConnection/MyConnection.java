/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package myConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Rokvic Nkola
 */
public class MyConnection {
    
    private static MyConnection instance;
    
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/java_parking_application";
    private String username = "root";
    private String password = "918273";
    
   private MyConnection() throws ClassNotFoundException {
		
		try {
			this.connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			System.out.println("Connection failed: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		
	}
	
	
	public static MyConnection getInstance() throws SQLException, ClassNotFoundException {
		if (instance == null) {
			instance = new MyConnection();
		}else if(instance.getConnection().isClosed()) {
			instance = new MyConnection();
		}
		return instance;
	}
	
	public Connection getConnection() {
		return connection;
	}
    
}

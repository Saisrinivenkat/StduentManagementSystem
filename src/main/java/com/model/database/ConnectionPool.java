package com.model.database;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionPool {
	
	// Attributes that will provide the required information to connect to the DB
	
	private static final String DB = "student_details";
	private static final String URL = "jdbc:mysql://localhost:3306/" + DB ;
	private static final String USER = "root";
	private static final String PASS = "";
	
	// The object dataSource will read the info required to access the DB 
	
	private static ConnectionPool dataSource = null;
	private Connection con = null;
	
	
	// Private constructor, as the intention with this design pattern (Singleton) 
	// is to force our app to have a single instance of this object
	
	private ConnectionPool() {
		
		initDataSource();
	}
	
	// We can omit the method and do this directly on the constructor
	// This method will retrieve the data into the pool to generate the connection
	
	private void initDataSource() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(URL, USER, PASS);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	// Verifies if there's an instance of this object, if not it generates one. Otherwise It will use the existing one.
	
	public static ConnectionPool getInstance() {
		
		if(dataSource == null) {
			dataSource = new ConnectionPool();
			return dataSource;
		} else {
			return dataSource;
		}
	}
	
	// Finally the method that initializes the connection.
	
	public Connection getConnection() throws SQLException {
		
		return this.con;
		
	}


	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		this.con.close();
	}
}

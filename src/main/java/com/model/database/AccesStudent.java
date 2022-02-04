package com.model.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.controller.bean.Student;

public class AccesStudent {
	
	
	private static AccesStudent instance;

	public static AccesStudent getInstance() throws Exception {
		
	 if(instance == null) {
		 instance = new AccesStudent();
	 }
	 return instance;
	}
	


	public List<Student> getStudentList() throws SQLException{
		Connection myConnection = null;
		String sql = "SELECT id, first_name, last_name, email FROM student;";
		CallableStatement query = null;
		ResultSet cursor = null;
		List<Student> result = new ArrayList<>();

		myConnection = ConnectionPool.getInstance().getConnection();
		query = myConnection.prepareCall(sql);
		cursor = query.executeQuery();
		while (cursor.next()) {
			result.add(new Student(cursor.getInt("id"),
					cursor.getString("first_name"),
					cursor.getString("last_name"),
					cursor.getString("email")));
		}
		return result;
	}

	public Student getStudent(int studentId) throws Exception {
		Connection myConnection = null;
		String sql = "select id, first_name, last_name, email\n" +
				"from student\n" +
				"where id = "+studentId+";";
		CallableStatement query = null;
		ResultSet cursor= null;
		Student result = null;

		myConnection = ConnectionPool.getInstance().getConnection();

			if(myConnection != null ) {
				query = myConnection.prepareCall(sql);
				cursor = query.executeQuery();
				if(cursor.next()) {
					int id = cursor.getInt("id");
					String fName = cursor.getString("first_name");
					String lName = cursor.getString("last_name");
					String email = cursor.getString("email");
					result = new Student(id,fName,lName,email);
					}
			} else {
				throw new Exception("Could not find student id: " + studentId);
			}
			return result;

	}
	
	public List<Student> searchStudents(String searchName) throws SQLException{

		Connection myConnection = null;
		String sql;
		CallableStatement query = null;
		ResultSet cursor = null;
		List<Student> result = new ArrayList<>();

		myConnection = ConnectionPool.getInstance().getConnection();

		if (searchName != null && searchName.trim().length() > 0) {

			sql = "SELECT id, first_name, last_name, email FROM student\n" +
					"where lower(first_name) like \""+searchName+"\" or lower(last_name) like \""+searchName+"\";";
			query = myConnection.prepareCall(sql);
		} else {
			sql = "SELECT id, first_name, last_name, email FROM student;";
			query = myConnection.prepareCall(sql);
		}
		cursor = query.executeQuery();
		while(cursor.next()) {
			result.add(new Student(cursor.getInt("id"),
								   cursor.getString("first_name"),
								   cursor.getString("last_name"),
								   cursor.getString("email")));}
		return result;
	}
	
	public boolean addStudent(Student student) throws SQLException {
		boolean result = false;
		Connection myConnection = null;
		String sql = "insert into student (\n" +
				"first_name,\n" +
				"last_name,\n" +
				"email\n" +
				")\n" +
				"VALUES(\n\"" +
				student.getfNname()
				+"\",\n\"" +
				student.getlName()
				+"\",\n\"" +
				student.getEmail()
				+"\"\n" +
				");";
		CallableStatement query = null;

		myConnection = ConnectionPool.getInstance().getConnection();

		if(myConnection != null ) {
			query = myConnection.prepareCall(sql);
			int update = query.executeUpdate();
			result = update > 0;
		}

		return result;
	}


	public void updateStudent(Student student) throws SQLException {
		Connection myConnection = null;
		String sql = "UPDATE `student`\n" +
				"SET\n" +
				"`first_name` = \""+student.getfNname()+"\",\n" +
				"`last_name` = \""+student.getlName()+"\",\n" +
				"`email` = \""+student.getEmail()+"\"\n" +
				"WHERE `id` = "+student.getId()+";";
		CallableStatement query = null;

		myConnection = ConnectionPool.getInstance().getConnection();

			if(myConnection != null ) {
				query = myConnection.prepareCall(sql);
				query.execute();
			}

	}
	
	public void deleteStudent(int studentId) throws SQLException {
		Connection myConnection = null;
		String sql = "DELETE from student\n" +
				"where id = "+studentId+";";
		CallableStatement query = null;

		myConnection = ConnectionPool.getInstance().getConnection();

			if(myConnection != null ) {
				query = myConnection.prepareCall(sql);
				query.execute();
			}
	}



}	

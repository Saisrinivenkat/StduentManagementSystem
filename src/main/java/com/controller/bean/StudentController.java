package com.controller.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.model.database.AccesStudent;

@ManagedBean (name="studentController")
@ViewScoped
public class StudentController implements Serializable{
	

	private List<Student> students;
	AccesStudent db;
	private String searchName;
	private List<SortMeta> sortBy;
	

	
	public StudentController() throws Exception {
		students = new ArrayList<>();
		
		db = AccesStudent.getInstance();
		
        sortBy = new ArrayList<>();
        sortBy.add(SortMeta.builder()
                .field("fName")
                .order(SortOrder.ASCENDING)
                .build());

        sortBy.add(SortMeta.builder()
                .field("lName")
                .order(SortOrder.ASCENDING)
                .build());
        sortBy.add(SortMeta.builder()
                .field("email")
                .order(SortOrder.ASCENDING)
                .build());
        sortBy.add(SortMeta.builder()
                .field("id")
                .order(SortOrder.ASCENDING)
                .build());
	}

	
	public void loadStudents() {
		
		students.clear();
		
		try {
			
			if(searchName != null && searchName.trim().length() >0) {
				students = db.searchStudents(searchName);
			} else {
				students = db.getStudentList();
			}

		} catch (Exception e) {
			addErrorMessage(e);
		} finally {
			searchName = null;
		}
	}
	
	public String loadStudent(int studentId) {
		
		
		try {
			Student student = db.getStudent(studentId);
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("student", student); 

		} catch (Exception e) {

			addErrorMessage(e);
			
			return null;
		}
		
		return "updatestudent.xhtml";
	}
	
	public String addStudentMultiple(Student student) {
		
		
		boolean result;
				
		try {
			
			result = db.addStudent(student);
			
			updateResult(result);
			
		} catch (Exception e) {
			addErrorMessage(e);
			
			return null;
		}
		
		return "add-student-form?faces-redirect=false";
	}


	public String updateStudent(Student student) {
		try {
			db.updateStudent(student);
		} catch (Exception e) {
			addErrorMessage(e);
			return null;
		}
		return "students-list?faces-redirect=true";
	}
	
	public String deleteStudent(int studentId) {
		try {
			db.deleteStudent(studentId);
		} catch (Exception e) {
			addErrorMessage(e);
			return null;
		}
		return "students-list";
	}
	
	private void addErrorMessage(Exception e) {
		FacesMessage message = new FacesMessage("Error: " + e.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
		
	}
	
	private void updateResult(Boolean result) {
		if(Boolean.TRUE.equals(result)) {
			FacesMessage message = new FacesMessage("Entry done correctly");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} else {
			FacesMessage message = new FacesMessage("Entry couldn't be done");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

	}
	
	public List<Student> getStudents() {return students;}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

}

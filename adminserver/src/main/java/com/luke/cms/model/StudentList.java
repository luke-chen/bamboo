package com.luke.cms.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "student-list")
public class StudentList {

	private List<Student> studentList;

	protected StudentList() {
	}

	public StudentList(List<Student> studentList) {
		this.studentList = studentList;
	}

	@XmlElement(name = "student")
	public List<Student> getStudentList() {
		return studentList;
	}
}
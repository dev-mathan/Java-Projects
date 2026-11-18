package com.example.evalpro.service;


import com.example.evalpro.dto.StudentDTO;
import com.example.evalpro.model.StudentEntity;
import com.example.evalpro.repository.AdminRepository;
import com.example.evalpro.repository.StudentRepository;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

@Service

public class AdminService {


	@Autowired
	private AdminRepository adminRepository;


	@Autowired
	private StudentRepository studentRepository;



	public void getDeleteById(Long id) {

		adminRepository.deleteById(id);

	}

	public List<StudentEntity> getAllStudents() {

		return studentRepository.findAll();
	}

//	public StudentEntity getStudentById(String studentId) {
//
//		System.out.println("Fetching student with ID: " + studentId);
//		return studentRepository.findByStudentId(studentId);
//					
//	}
//
//
//	 public void deleteStudentByStudentId(String studentId) {
//	        studentRepository.deleteByStudentId(studentId);
//	    }
	  
}

package com.example.evalpro.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.print.attribute.standard.MediaSize.Other;

import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.evalpro.model.StudentEntity;
import com.example.evalpro.repository.*;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private AdminRepository adminRepository;
	
	 public StudentEntity saveStudent(com.example.evalpro.dto.StudentDTO dto) {
	    
		    com.example.evalpro.model.StudentEntity student= new StudentEntity();
            student.setName(dto.getName());
		    student.setEmail(dto.getEmail());
		    student.setDepartment(dto.getDepartment());
		    student.setYear(dto.getYear());
		    student.setPassword(dto.getPassword());
	        return studentRepository.save(student);
	    }
	

	 public StudentEntity validateStudentLogin(String email, String password) {

		    StudentEntity student = studentRepository.findByEmail(email);

		    if (student != null && student.getPassword().equals(password)) {
		        return student;
		    }

		    return null;
		}



	 public List<StudentEntity> getAllStudents() {
	        return studentRepository.findAll();
	    }

	 

	
}

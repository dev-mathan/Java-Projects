package com.example.evalpro.service;


import java.util.ArrayList;
import java.util.List;

import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.evalpro.dto.staffDTO;
import com.example.evalpro.model.staffEntity;
import com.example.evalpro.repository.StudentRepository;
import com.example.evalpro.repository.staffRepository;

@Service
public class staffservice {

	
	
	@Autowired
	private StudentRepository studentRepository;
	
	

	@Autowired
	private staffRepository staffrepo;
	
    public String registerStaff(staffDTO dto) {

        staffEntity staff = staffRepository.findByEmail(dto.getEmail());

        if (staff != null) {
            return "Email already exists!";
        }

        // Create new staff
        staffEntity staff1 = new staffEntity();
        staff1.setName(dto.getName());
        staff1.setEmail(dto.getEmail());
        staff1.setMobile(dto.getMobile());
        staff1.setDepartment(dto.getDepartment());
        staff1.setPassword(dto.getPassword()); // Plain password
        staff1.setRole("STAFF");

        staffrepo.save(staff1);

        return "Staff Registered Successfully";
    }
	

 

    public staffEntity loginStaff(String email, String password, String department) {

        if(email == null || password == null || department == null) {
            return null;
        }

        staffEntity staff = staffrepo
                .findByEmailAndPasswordAndDepartment(email, password, department);

        if (staff == null) {
            return null;
        }

        return staff;
    }



	public Object getAllStass() {
        return studentRepository.findAll();
	}


	public List<staffEntity> getAllStaff() {
		return staffrepo.findAll();
	}

}

package com.example.evalpro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.evalpro.model.staffEntity;

import java.util.Optional;

@Repository
public interface staffRepository extends JpaRepository<staffEntity, Long> {

	   staffEntity findByEmailAndPasswordAndDepartment(
	            String email,
	            String password,
	            String department
	    );

	static staffEntity findByEmail(String email) {
		return null;
	}
}



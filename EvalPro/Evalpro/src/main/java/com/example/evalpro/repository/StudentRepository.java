package com.example.evalpro.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.example.evalpro.model.StudentEntity;

import jakarta.transaction.Transactional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {

 StudentEntity findByEmail(String email);

}

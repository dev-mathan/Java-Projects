package com.example.evalpro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.evalpro.model.MCQEntity;

public interface MCQRepository extends JpaRepository<MCQEntity, Long> {

	List<MCQEntity> findByDepartment(String department);

}

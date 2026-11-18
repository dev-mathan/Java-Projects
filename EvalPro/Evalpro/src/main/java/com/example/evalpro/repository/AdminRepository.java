package com.example.evalpro.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.evalpro.model.StudentEntity;


@Repository
public interface AdminRepository extends JpaRepository<StudentEntity, Long> {


}

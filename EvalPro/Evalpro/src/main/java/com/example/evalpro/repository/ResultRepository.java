package com.example.evalpro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.evalpro.model.ResultEntity;
import com.example.evalpro.model.StudentEntity;

@Repository
public interface ResultRepository extends JpaRepository<ResultEntity, Long> {

    List<ResultEntity> findByStudent_Department(String department);

    List<ResultEntity> findByStudent_Id(Long studentId);

}

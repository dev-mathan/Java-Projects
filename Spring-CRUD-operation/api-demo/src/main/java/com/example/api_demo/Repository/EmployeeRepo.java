package com.example.api_demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import com.example.api_demo.Entity.Employee;

@Repository
public interface EmployeeRepo  extends JpaRepository<Employee, Integer>{

}

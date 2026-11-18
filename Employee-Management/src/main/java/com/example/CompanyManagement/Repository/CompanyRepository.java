package com.example.CompanyManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CompanyManagement.Entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long>{

}

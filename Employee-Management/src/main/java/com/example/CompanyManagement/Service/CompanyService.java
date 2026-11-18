package com.example.CompanyManagement.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CompanyManagement.Entity.Company;
import com.example.CompanyManagement.Repository.CompanyRepository;

@Service
public class CompanyService {
	
	@Autowired
	private CompanyRepository companyRepository;
	
	//Create
	public Company saveCompany (Company company) {
		return companyRepository.save(company);
	}
	
	//Read
	public Company getCompanyById (Long companyid) {
		return companyRepository.findById(companyid)
				.orElseThrow(()-> new RuntimeException("id not fount"));
	}
	
	public String updatecompany(long id, Company company) {

	    Company existingCompany = companyRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Company not found"));

	    existingCompany.setCompanyname(company.getCompanyname());
	    existingCompany.setLocation(company.getLocation());
	    existingCompany.setCompanytype(company.getCompanytype());

	    companyRepository.save(existingCompany);

	    return "Data Updated";
	}
	
	//Delete
		public String deleteCompanyById (Long companyid) {
		 companyRepository.deleteById(companyid);
		 
		 return "Data Deleted";
		}
		
		public List<Company> getAllCompany () {
			return companyRepository.findAll();
		}

}

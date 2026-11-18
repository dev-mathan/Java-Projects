package com.example.CompanyManagement.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.CompanyManagement.Entity.Company;
import com.example.CompanyManagement.Service.CompanyService;

@RestController
@RequestMapping("/api/company")
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;
	
		//save
	    @PostMapping("/savecompany")
		public ResponseEntity<Company> saveCompany (@RequestBody Company company) {
			return new ResponseEntity<Company>(companyService.saveCompany(company),HttpStatus.CREATED);
		}
		//gatbyid
		@GetMapping("/getbyid/{companyid}")
		public ResponseEntity<Company> getCompanyById (@PathVariable Long companyid) {
			return  new ResponseEntity<Company>(companyService.getCompanyById(companyid),HttpStatus.OK);
					
		}
		//update
		@PutMapping("/update/id/{id}")
		public ResponseEntity<String> updatecompany (@PathVariable long id,@RequestBody Company company) {
		
			return 	new ResponseEntity<String>(companyService.updatecompany(id, company),HttpStatus.OK);
		
		} 
		
		//Delete
		@DeleteMapping("/delete/{companyid}")
		public ResponseEntity<String> deleteCompanyById (@PathVariable Long companyid) {
		return new ResponseEntity<String>(companyService.deleteCompanyById(companyid),HttpStatus.OK);
		}
		@GetMapping("findall")
		public ResponseEntity<List<Company>> getAllCompany () {
			return new ResponseEntity<List<Company>>(companyService.getAllCompany(),HttpStatus.OK);
		}

}

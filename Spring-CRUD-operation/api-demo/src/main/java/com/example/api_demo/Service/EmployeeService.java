package com.example.api_demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.stereotype.Service;

import com.example.api_demo.Entity.Employee;
import com.example.api_demo.Repository.EmployeeRepo;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepo employeeRepo;
	
	//add the eployee details
	public Employee saveEmployee(Employee employee) {
		
		if(employeeRepo.existsById(employee.getEid())) {
			throw new RuntimeException("Employee ID already exists: " + employee.getEid());
		}
		
			return employeeRepo.save(employee);
	}
	
	
	//get all the employee details
	public List<Employee> getAllEmployees (){
		return  employeeRepo.findAll();
	}
	
	//Fetch the data based on Id
	public Employee getById(Integer id) {
	    return employeeRepo.findById(id).orElseThrow(
	    		() -> new ResponseStatusException (HttpStatus.NOT_FOUND,"Employee not found"));
	}
	
	//Update the employee
	public Employee updateEmployee(Integer id, Employee employee) {

	    Employee existingEmployee = employeeRepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("Employee not found"));

	    existingEmployee.setName(employee.getName());
	    existingEmployee.setJob(employee.getJob());
	    existingEmployee.setGender(employee.getGender());
	    existingEmployee.setSalary(employee.getSalary());

	    return employeeRepo.save(existingEmployee);
	}
	
	//delete the data by id
	public void deleteEmpById(Integer id) {
	 employeeRepo.deleteById(id);
	}
}



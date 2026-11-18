package com.example.api_demo.Controller;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import com.example.api_demo.Entity.Employee;
import com.example.api_demo.Service.EmployeeService;

@RestController
@RequestMapping("/emp/api")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeservice;
	
	@PostMapping("/emp")
	public Employee addEmploye(@RequestBody Employee employee) {
		return employeeservice.saveEmployee(employee);
		
	}
	
	@GetMapping("/empdetail")
	public List<Employee> getEmployees() {
	    return employeeservice.getAllEmployees();
	}
	
	@GetMapping("/getbyid/{id}")
	public Employee getById(@PathVariable Integer id) {
		return employeeservice.getById(id);
	}
	
	@PutMapping("/update/{id}")
	public Employee updateEmployee(@PathVariable Integer id,@RequestBody Employee employee) {

	    return employeeservice.updateEmployee(id, employee);
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteEmployee(@PathVariable int id) {
		employeeservice.deleteEmpById(id);
		return "Employee deleted successfully";
	}
}
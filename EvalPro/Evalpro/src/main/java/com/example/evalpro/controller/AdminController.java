package com.example.evalpro.controller;



import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.evalpro.model.ResultEntity;
import com.example.evalpro.model.staffEntity;
import com.example.evalpro.repository.ResultRepository;
import com.example.evalpro.service.AdminService;
import com.example.evalpro.service.ResultService;
import com.example.evalpro.service.StudentService;
import com.example.evalpro.service.staffservice;


@Controller
public class AdminController {
	
	
	@Autowired
	
	private final ResultRepository resultrepo;
	@Autowired
	  private final ResultService rsulservice;
	  private final StudentService studentservice;
	  private final AdminService adminService;
      private final staffservice staffservice;
	    public AdminController(AdminService adminService, StudentService studentservice, staffservice staffservice, ResultService rsulservice, ResultRepository resultrepo) { 
			this.resultrepo = resultrepo;
			this.rsulservice = rsulservice;
			this.studentservice = studentservice;
			this.adminService = adminService;
			this.staffservice = staffservice;
	    }

	  @PostMapping("/admin/login")
	    public String adminLogin(@RequestParam("username") String username,
	                             @RequestParam("password") String password) {

	       
	        if ("admin@gmail.com".equals(username) && "admin123".equals(password)) {
	            return "adminhome";
	        } else {
	            return "redirect:/adminlogin?error=true";
	        }
	    }
	  
	  
	  @GetMapping("/admin/logout")
	  public String adminLogout() {
		  return "index";
	  }
	  
	  
	  @GetMapping("/admin/back1")
	  public String adminBack1() {
		  return "adminhome";
	  }

	    
	   @GetMapping("/students")
	    public String viewStudents(Model model) {
	        model.addAttribute("students", studentservice.getAllStudents());
	        return "viewallstudent";
	    }
		  
	   @GetMapping("/viewstaff")
	   public String viewStaff(Model model) {
	       List<staffEntity> staffList = staffservice.getAllStaff(); 
	       model.addAttribute("staffList", staffList);
	       return "viewallstaff";
	   }


	   @GetMapping("/admin/marks")
	    public String showAllMarks(Model model) {
	        List<ResultEntity> results = rsulservice.getAllResults();
	        model.addAttribute("marksList", results);
	        return "viewallstudentmarks";
	    }
	   
	   @GetMapping("/admin/analysis")
	   public String showMarksAnalysis(Model model) {

	       List<ResultEntity> marksList = resultrepo.findAll();

	       Map<String, Object> analysis = rsulservice.calculateAnalysis(marksList);

	       model.addAttribute("marksList", marksList);
	       model.addAttribute("averagePercentage", analysis.get("averagePercentage"));
	       model.addAttribute("passCount", analysis.get("passCount"));
	       model.addAttribute("passPercentage", analysis.get("passPercentage"));
	       model.addAttribute("highestScore", analysis.get("highestScore"));

	       return "adminanalysis";  
	   }

}

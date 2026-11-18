package com.example.evalpro.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.evalpro.dto.staffDTO;
import com.example.evalpro.model.MCQEntity;
import com.example.evalpro.model.ResultEntity;
import com.example.evalpro.model.staffEntity;
import com.example.evalpro.service.Questionservice;
import com.example.evalpro.service.ResultService;
import com.example.evalpro.service.staffservice;

import jakarta.servlet.http.HttpSession;

@Controller
public class staffController {
	
	
	@Autowired
	private final ResultService resultservice;
	
	 @Autowired
	    private  staffservice staffservice;

	 @Autowired
	 private Questionservice questionservice;
	
	 public staffController(staffservice staffservice, ResultService resultservice) {
	        this.resultservice = resultservice;
			this.staffservice = staffservice;
	    }

	 @PostMapping("/staff/register")
	 public String register(@ModelAttribute("staff") staffDTO dto,
	                        Model model) {

	     String msg = staffservice.registerStaff(dto);
	     model.addAttribute("msg", msg);

	     return "staffreg";   // your registration page name
	 }
	    @GetMapping("/staff/login")
	    public String showLoginPage() {
	        return "stafflogin";
	    }

	    @PostMapping("/staff/login")
	    public String loginStaff(@RequestParam String email,
	                             @RequestParam String password,
	                             @RequestParam String department,
	                             Model model,HttpSession session) {

	        staffEntity result = staffservice.loginStaff(email, password, department);

	        if (result != null) {
	             session.setAttribute("staffDept", result.getDepartment());
	            return "redirect:/staff/home";
	        }

	        model.addAttribute("errorMessage", result);
	        return "stafflogin";
	    }

	    @GetMapping("/staff/home")
	    public String homePage() {
	        return "staffhonme";
	    }

	
	 
	 @GetMapping("/register")
	 public String showRegister(Model model) {
	     model.addAttribute("staff", new staffEntity()); 
	     return "staffreg";
	 }

	 @GetMapping("/parent/logout")
	  public String parentLogout() {
		  return "index";
	  }
	 

	 @GetMapping("/staff/questions")
	 public String addquestionspage() {
		 return "addQuestions";
	 }
	 
	   @GetMapping("/staff/marks")
	    public String staffmarks(HttpSession session, Model model) {

	        // ✅ Get logged-in staff department
		    String dept = (String) session.getAttribute("staffDept");

	        // 🔥 Example (TEMP if session not set)
	        if (dept == null) {
	            return "redirect:/staff/login"; // or show error
	        }

	        List<ResultEntity> marksList = resultservice.getMarksByDepartment(dept);

	        model.addAttribute("marksList", marksList);

	        return "staffviewmarks";
	    }

	 
	 @GetMapping("/staff/viewstudentexam")
	 public String getstudentexam() {
		 return "viewstudentexam";
	 }
	 
	    @PostMapping("/generate")
	    public String generateQuestions(
	            @RequestParam String topic,
	            @RequestParam String summary,
	            @RequestParam String department,
	            Model model) {

	        List<MCQEntity> mcqs = questionservice.generateMCQs(topic, summary,department);
	        model.addAttribute("mcqs", mcqs);

	        return "addQuestions";
	    }
	    
	    @GetMapping("/results")
	    public String viewResults(Model model) {
	        List<ResultEntity> results = resultservice.getAllResults1();
	        model.addAttribute("results", results);
	        return "results";   // results.html
	    }
}

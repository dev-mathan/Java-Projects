package com.example.evalpro.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.evalpro.dto.StudentDTO;
import com.example.evalpro.model.MCQEntity;
import com.example.evalpro.model.ResultEntity;
import com.example.evalpro.model.StudentEntity;
import com.example.evalpro.repository.MCQRepository;
import com.example.evalpro.repository.ResultRepository;
import com.example.evalpro.repository.StudentRepository;
import com.example.evalpro.service.ResultService;
import com.example.evalpro.service.StudentService;

import jakarta.persistence.criteria.Path;
import jakarta.servlet.http.HttpSession;



@Controller
public class SudentController {
	
	@Autowired
	MCQRepository mcqrepo;
	
	@Autowired
	private ResultRepository resultRepository;
	private final ResultService resultservice;
	private final StudentService studentService;
    private final StudentRepository studentRepository;
	 public SudentController(StudentService studentService, StudentRepository studentRepository, ResultService resultservice) {
			this.resultservice = resultservice;
			this.studentService = studentService;
			this.studentRepository = studentRepository;
	    }
	 
	    @GetMapping("/student/register")
	    public String showRegisterPage(Model model) {
	        model.addAttribute("student", new StudentEntity());
	        return "studentregister";
	    }
	  @PostMapping("/student/register")
	    public String saveStudent(StudentDTO dto) {

		  studentService.saveStudent(dto);
	       
	        return "studentlogin";
	    }

	  
	  @PostMapping("/student/login")
	  public String studentLogin(@RequestParam("email") String email,
	                             @RequestParam("password") String password,
	                             Model model,
	                             HttpSession session) {

	      StudentEntity student = studentService.validateStudentLogin(email, password);
	      if (student != null) {
	          session.setAttribute("student", student);
	          model.addAttribute("student", student); // ✅ ADD THIS LINE

	          return "studenthome";
	      } else {
	          model.addAttribute("error", "Invalid email or password");
	          return "studentlogin";
	      }
	  }
  
	  

	
	  @GetMapping("/student/home")
	  public String home(HttpSession session, Model model) {

	      StudentEntity student = (StudentEntity) session.getAttribute("student");
	      model.addAttribute("student", student);

	      return "studenthome";
	  }

	  
	  @GetMapping("/student/back3")
	  public String studentBack3() {
		  return "redirect:/student/applyleave";
	  }
	  
	  @GetMapping("/student/back1")
	  public String studentBack1() {
		  return "redirect:/student/viewrooms";
	  }
	  
	  @GetMapping("/student/back2")
	  public String studentBack2() {
		  return "redirect:/student/complaints";
	  }
	  
	  @GetMapping("/student/logout")
	  public String studentlogout() {
		  return "index";
	  }
	  
	  
 	  @GetMapping("/student/viewquestions")
	  public String viewquestions(Model model) {
		  model.addAttribute("mcqs", mcqrepo.findAll());
		  return "viewquestions";
	  }
	
	  
 	 @GetMapping("/student/attendexam")
 	public String attendExam(Model model, HttpSession session) {

 	    StudentEntity student =
 	            (StudentEntity) session.getAttribute("student");

 	    if (student == null) {
 	        return "redirect:/student/login";
 	    }

 	    List<MCQEntity> mcqs =
 	            mcqrepo.findByDepartment(student.getDepartment());

 	    model.addAttribute("mcqs", mcqs);

 	    return "attendexam";
 	}

 	@GetMapping("/student/result")
 	public String showResult(HttpSession session, Model model) {

 	    Long resultId = (Long) session.getAttribute("resultId");

 	    if (resultId == null) {
 	        return "redirect:/student/attendexam";
 	    }

 	    ResultEntity result = resultRepository.findById(resultId).orElse(null);

 	    if (result == null) {
 	        return "redirect:/student/attendexam";
 	    }

 	    model.addAttribute("score", result.getScore());
 	    model.addAttribute("total", result.getTotal());
 	    model.addAttribute("examDate", result.getExamDate());

 	    session.removeAttribute("resultId");

 	    return "viewmarks";
 	}

//	  @GetMapping("/student/result")
//	  public String showResult(@RequestParam int score,
//	                           @RequestParam int total,
//	                           Model model) {
//
//	      model.addAttribute("score", score);
//	      model.addAttribute("total", total);
//
//	      return "viewmarks";
//	  }

//	  @PostMapping("/student/submitExam")
//	  public String submitExam(@RequestParam Map<String,String> allParams,
//	                           HttpSession session) {
//
//	      StudentEntity student =
//	              (StudentEntity) session.getAttribute("loggedInStudent");
//
//	      if (student == null) {
//	          return "redirect:/student/login";
//	      }
//
//	      List<MCQEntity> mcqs =
//	              mcqrepo.findByDepartment(student.getDepartment());
//
//	      int score = 0;
//
//	      for (MCQEntity mcq : mcqs) {
//
//	          String paramName = "answer_" + mcq.getId();
//
//	          if (allParams.containsKey(paramName)) {
//
//	              String selected = allParams.get(paramName);
//
//	              if (selected.equalsIgnoreCase(mcq.getCorrectAnswer())) {
//	                  score++;
//	              }
//	          }
//	      }
//
//	      return "redirect:/student/result?score=" + score + "&total=" + mcqs.size();
//	  }

	  @PostMapping("/student/submitExam")
	  public String submitExam(@RequestParam Map<String,String> allParams,
	                           HttpSession session) {

	      StudentEntity student =
	              (StudentEntity) session.getAttribute("student");

	      if (student == null) {
	          return "redirect:/student/login";
	      }

	      List<MCQEntity> mcqs =
	              mcqrepo.findByDepartment(student.getDepartment());

	      int score = 0;

	      for (MCQEntity mcq : mcqs) {

	          String paramName = "answer_" + mcq.getId();

	          if (allParams.containsKey(paramName)) {

	              String selected = allParams.get(paramName);

	              if (selected.equalsIgnoreCase(mcq.getCorrectAnswer())) {
	                  score++;
	              }
	          }
	      }

	      // 🔥 SAVE RESULT IN DATABASE
	      ResultEntity result = new ResultEntity();
	      result.setStudent(student);
	      result.setScore(score);
	      result.setTotal(mcqs.size());
	      result.setExamDate(LocalDateTime.now());

	      resultRepository.save(result);

	      session.setAttribute("resultId", result.getId());

	      return "redirect:/student/result";
	  }
	  
	  @GetMapping("/student/myresults")
	  public String viewMyResults(HttpSession session, Model model) {

	      StudentEntity student = (StudentEntity) session.getAttribute("student");

	      if (student == null) {
	          return "redirect:/student/login";
	      }

	      List<ResultEntity> results = resultservice.getResultsByStudentId(student.getId());

	      model.addAttribute("marksList", results);
	      model.addAttribute("student", student);

	      return "studentviewmarks";
	  }

	  
	}


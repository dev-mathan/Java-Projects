package com.example.evalpro.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class HomeController {
	
    @GetMapping("/index")
    public String index() {
        return "index";
    }
    
    
    @GetMapping("/studentlogin")
    public String studentLogin() {
        return "studentlogin";
    }
    
    
    @GetMapping("/studentregister")
    public String studentRegister() {
        return "studentregister";
    }
    
    
    @GetMapping("/adminlogin")
    public String adminLogin() {
        return "adminlogin";
    }
    
    
    @GetMapping("/parentregister")
    public String parentRegister() {
        return "parentreg";
    }

}

package com.example.evalpro.controller;

import com.example.evalpro.model.MCQEntity;
import com.example.evalpro.repository.MCQRepository;
import com.example.evalpro.service.Questionservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private Questionservice questionservice;

    @Autowired
    private MCQRepository mcqRepository;

    @GetMapping("/generate")
    public String showPage() {
        return "generateQuestions";
    }

    @PostMapping("/generate")
    public String generateQuestions(
            @RequestParam String topic,
            @RequestParam String summary,
            @RequestParam String department,
            Model model) throws Exception {

        List<MCQEntity> mcqList = questionservice.generateMCQs(topic, summary,department);

        model.addAttribute("mcqList", mcqList);

        return "addQuestions";
    }

}

package com.example.evalpro.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.evalpro.model.StudentEntity;
import com.example.evalpro.repository.ResultRepository;

@Service
public class ResultService<ResultEntity> {
	
	   @Autowired
	    private ResultRepository resultRepository;

	    public List<com.example.evalpro.model.ResultEntity> getAllResults() {
	        return resultRepository.findAll();  
	    }

	    public Map<String, Object> calculateAnalysis(List<com.example.evalpro.model.ResultEntity> marksList) {

	        Map<String, Object> analysis = new HashMap<>();

	        if (marksList == null || marksList.isEmpty()) {
	            analysis.put("averagePercentage", 0);
	            analysis.put("passCount", 0);
	            analysis.put("passPercentage", 0);
	            analysis.put("highestScore", 0);
	            return analysis;
	        }

	        double totalPercentage = 0;
	        int passCount = 0;
	        int highestScore = 0;

	        for (com.example.evalpro.model.ResultEntity mark : marksList) {

	            if (mark.getTotal() > 0) {

	                double percentage = (mark.getScore() * 100.0) / mark.getTotal();
	                totalPercentage += percentage;

	                if (percentage >= 40) {
	                    passCount++;
	                }

	                if (mark.getScore() > highestScore) {
	                    highestScore = mark.getScore();
	                }
	            }
	        }

	        double averagePercentage = totalPercentage / marksList.size();
	        double passPercentage = (passCount * 100.0) / marksList.size();

	        analysis.put("averagePercentage", String.format("%.1f", averagePercentage));
	        analysis.put("passCount", passCount);
	        analysis.put("passPercentage", String.format("%.1f", passPercentage));
	        analysis.put("highestScore", highestScore);

	        return analysis;
	    }

		public List<com.example.evalpro.model.ResultEntity> getAllResults1() {
			 return resultRepository.findAll();
			 }
		
		   public List<com.example.evalpro.model.ResultEntity> getMarksByDepartment(String dept) {
		        return resultRepository.findByStudent_Department(dept);
		    }

		   public List<com.example.evalpro.model.ResultEntity> getResultsByStudentId(Long studentId) {
		        return resultRepository.findByStudent_Id(studentId);
		    }

}

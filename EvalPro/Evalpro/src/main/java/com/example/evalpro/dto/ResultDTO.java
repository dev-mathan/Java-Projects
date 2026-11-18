package com.example.evalpro.dto;

import java.time.LocalDateTime;

import com.example.evalpro.model.StudentEntity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class ResultDTO {

	 private Long id;

	    private int score;
	    private int total;

	    private LocalDateTime examDate;
	    private StudentEntity student;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public int getScore() {
			return score;
		}
		public void setScore(int score) {
			this.score = score;
		}
		public int getTotal() {
			return total;
		}
		public void setTotal(int total) {
			this.total = total;
		}
		public LocalDateTime getExamDate() {
			return examDate;
		}
		public void setExamDate(LocalDateTime examDate) {
			this.examDate = examDate;
		}
		public StudentEntity getStudent() {
			return student;
		}
		public void setStudent(StudentEntity student) {
			this.student = student;
		}
	    
	    
	    
}

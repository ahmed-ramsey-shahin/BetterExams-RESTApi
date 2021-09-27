package com.ramsey.betterexamsrestapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Teacher {
	
	@Id
	private Long id;
	
	@OneToOne(targetEntity = User.class)
	private User user;
	
	@ManyToMany
	@ToString.Exclude
	@JsonIgnore
	private List<Student> students;
	
	@OneToMany
	@ToString.Exclude
	@JsonIgnore
	private List<Exam> exams;
	
	{
		
		students = new ArrayList<>();
		exams = new ArrayList<>();
		
	}
	
}

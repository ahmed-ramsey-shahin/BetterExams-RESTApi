package com.ramsey.betterexamsrestapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class Teacher {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
	@NonNull
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

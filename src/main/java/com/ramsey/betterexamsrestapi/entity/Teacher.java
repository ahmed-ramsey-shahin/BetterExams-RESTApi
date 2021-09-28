package com.ramsey.betterexamsrestapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
	
	@NonNull
	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
	private User user;
	
	@ManyToMany
	@JsonIgnore
	@ToString.Exclude
	private Set<Student> students;
	
	@OneToMany
	@JsonIgnore
	@ToString.Exclude
	private Set<Exam> exams;
	
	{
		
		students = new HashSet<>();
		exams = new HashSet<>();
		
	}
	
}

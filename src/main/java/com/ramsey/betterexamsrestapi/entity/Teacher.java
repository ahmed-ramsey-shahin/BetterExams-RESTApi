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
	
	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
	@NonNull
	private User user;
	
	@ManyToMany
	@ToString.Exclude
	@JsonIgnore
	private Set<Student> students;
	
	@OneToMany
	@ToString.Exclude
	@JsonIgnore
	private Set<Exam> exams;
	
	{
		
		students = new HashSet<>();
		exams = new HashSet<>();
		
	}
	
}

package com.ramsey.betterexamsrestapi.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class Exam {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NonNull
	@Pattern(regexp = "^(?=.*[a-z])[^<>]{5,}$")
	private String name;
	
	@Min(20)
	private Integer timeInMinutes;
	
	@Min(1)
	private Double requiredScore;
	
	@ManyToMany(targetEntity = Question.class, cascade = CascadeType.ALL)
	@ToString.Exclude
	@Size(min = 5)
	private Set<Question> questions;
	
	{
		
		questions = new HashSet<>();
		
	}
	
}

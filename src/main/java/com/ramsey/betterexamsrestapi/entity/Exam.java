package com.ramsey.betterexamsrestapi.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class Exam {
	
	@Id
	private Long id;
	
	@NonNull
	@Pattern(regexp = "^(?=.*[a-z])[^<>]{5,}$")
	private String name;
	
	@Min(20)
	private Integer timeInMinutes;
	
	@Min(1)
	private Double requiredScore;
	
	@ManyToMany(targetEntity = Question.class)
	@ToString.Exclude
	@Size(min = 5)
	private List<Question> questions;
	
	{
		
		questions = new ArrayList<>();
		
	}
	
}

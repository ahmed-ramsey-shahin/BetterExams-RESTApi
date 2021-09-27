package com.ramsey.betterexamsrestapi.entity;

import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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
public class Question {
	
	@Id
	private Long id;
	
	@NonNull
	@Pattern(regexp = "^(?=.*[a-z])[^<>]{5,}$")
	private String question;
	
	@ElementCollection
	@Size(min = 2)
	private List<String> answers;
	
	@NonNull
	@Min(1)
	private Integer score;
	
	@NotBlank
	private String rightAnswer;
	
	{
		
		answers = new ArrayList<>();
		
	}
	
}

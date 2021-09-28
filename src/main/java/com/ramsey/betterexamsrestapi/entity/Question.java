package com.ramsey.betterexamsrestapi.entity;

import lombok.*;

import javax.persistence.*;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NonNull
	@Pattern(regexp = "^(?=.*[a-z])[^<>]{5,}$")
	private String question;
	
	@Size(min = 2)
	@ElementCollection
	private List<String> answers;
	
	@Min(1)
	@NonNull
	private Integer score;
	
	@NotBlank
	private String rightAnswer;
	
	{
		
		answers = new ArrayList<>();
		
	}
	
}

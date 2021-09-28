package com.ramsey.betterexamsrestapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@JsonIgnoreProperties(value = "date", allowGetters = true)
public class ExamResult {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Min(0)
	@NonNull
	private Integer score;
	
	@NonNull
	@ManyToOne(targetEntity = Exam.class, fetch = FetchType.EAGER)
	private Exam exm;
	
	@NonNull
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Min(0)
	@NonNull
	private Integer noOfSolvedQuestions;
	
	{
		
		date = new Date();
		
	}
	
}

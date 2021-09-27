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
	
	@NonNull
	@Min(0)
	private Integer score;
	
	@ManyToOne(targetEntity = Exam.class, fetch = FetchType.EAGER)
	@NonNull
	private Exam exm;
	
	@NonNull
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@NonNull
	@Min(0)
	private Integer noOfSolvedQuestions;
	
	{
		
		date = new Date();
		
	}
	
}

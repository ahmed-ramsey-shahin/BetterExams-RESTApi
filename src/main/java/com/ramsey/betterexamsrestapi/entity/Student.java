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
public class Student {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
	@NonNull
	private User user;
	
	@OneToMany(targetEntity = ExamResult.class)
	@ToString.Exclude
	@JsonIgnore
	private List<ExamResult> results;
	
	{
		
		results = new ArrayList<>();
		
	}
	
}

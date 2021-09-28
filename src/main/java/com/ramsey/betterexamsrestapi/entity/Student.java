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
public class Student {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NonNull
	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
	private User user;
	
	@JsonIgnore
	@ToString.Exclude
	@OneToMany(targetEntity = ExamResult.class)
	private Set<ExamResult> results;
	
	{
		
		results = new HashSet<>();
		
	}
	
}

package com.ramsey.betterexamsrestapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Student {
	
	@Id
	private Long id;
	
	@OneToOne(targetEntity = User.class)
	private User user;
	
	@OneToMany(targetEntity = ExamResult.class)
	@ToString.Exclude
	@JsonIgnore
	private List<ExamResult> results;
	
	{
		
		results = new ArrayList<>();
		
	}
	
}

package com.ramsey.betterexamsrestapi.resource;

import com.ramsey.betterexamsrestapi.entity.Exam;
import com.ramsey.betterexamsrestapi.service.business.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("exams")
public class ExamResource {
	
	private final ExamService examService;
	
	@PostMapping
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> addExam(
			@RequestBody Exam exam,
			Authentication authentication
	) {
		
		return ResponseEntity.ok(examService.addExam(exam, authentication.getName()));
		
	}
	
}

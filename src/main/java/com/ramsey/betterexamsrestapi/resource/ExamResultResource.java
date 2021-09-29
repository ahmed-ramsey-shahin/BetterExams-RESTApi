package com.ramsey.betterexamsrestapi.resource;

import com.ramsey.betterexamsrestapi.entity.ExamResult;
import com.ramsey.betterexamsrestapi.entity.User;
import com.ramsey.betterexamsrestapi.service.business.ExamResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("results")
public class ExamResultResource {
	
	private final ExamResultService examResultService;
	
	@GetMapping
	@PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
	public ResponseEntity<?> getResults(
			@RequestParam(required = false, defaultValue = "-1") Integer limit,
			Authentication authentication
	) {
		
		List<ExamResult> examResults;
		
		if(limit <= 0) {
			
			examResults = examResultService.searchExams(
					authentication.getName(),
					((User) authentication.getDetails()).getType()
			);
			
		} else {
			
			examResults = examResultService.searchExams(
					authentication.getName(),
					((User) authentication.getDetails()).getType(),
					limit
			);
			
		}
		
		return ResponseEntity.ok(examResults);
		
	}
	
	@PostMapping
	@PreAuthorize("hasRole('STUDENT')")
	public ResponseEntity<?> postResult(
			@RequestBody ExamResult examResult,
			Authentication authentication
	) {
		
		return ResponseEntity.ok(examResultService.addExamResult(examResult, authentication.getName()));
		
	}
	
}

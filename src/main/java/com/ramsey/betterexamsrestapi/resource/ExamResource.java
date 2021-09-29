package com.ramsey.betterexamsrestapi.resource;

import com.ramsey.betterexamsrestapi.entity.Exam;
import com.ramsey.betterexamsrestapi.entity.User;
import com.ramsey.betterexamsrestapi.service.business.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("exams")
public class ExamResource {
	
	private final ExamService examService;
	
	@GetMapping
	@PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
	public ResponseEntity<?> searchExams(
			@RequestParam(required = false, defaultValue = "") String name,
			@RequestParam(required = false, defaultValue = "-1") Integer limit,
			Authentication authentication
	) {
		
		List<Exam> exams;
		
		if(limit <= 0) {
			
			exams = examService.searchExam(
					authentication.getName(),
					((User) authentication.getDetails()).getType(),
					name
			);
			
		} else {
			
			exams = examService.searchExam(
					authentication.getName(),
					((User) authentication.getDetails()).getType(),
					name,
					limit
			);
			
		}
		
		return ResponseEntity.ok(exams);
		
	}
	
	@GetMapping("{examId}")
	@PreAuthorize("@examService.canAccess(authentication.details, #examId)")
	public ResponseEntity<?> getExam(
			@PathVariable Long examId
	) {
		
		return ResponseEntity.ok(examService.get(examId));
		
	}
	
	@PostMapping
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> addExam(
			@RequestBody Exam exam,
			Authentication authentication
	) {
		
		return ResponseEntity.ok(examService.addExam(exam, authentication.getName()));
		
	}
	
	@PutMapping("{examId}")
	@PreAuthorize("@examService.canAccess(authentication.details, #examId)")
	public ResponseEntity<?> updateExam(
			@PathVariable Long examId,
			@RequestBody Exam exam
	) {
		
		return ResponseEntity.ok(examService.updateExam(exam, examId));
		
	}
	
}

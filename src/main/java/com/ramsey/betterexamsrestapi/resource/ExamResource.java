package com.ramsey.betterexamsrestapi.resource;

import com.ramsey.betterexamsrestapi.entity.Exam;
import com.ramsey.betterexamsrestapi.entity.UserType;
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
	public ResponseEntity<?> searchExams(
			@RequestParam(required = false, defaultValue = "") String name,
			@RequestParam(required = false, defaultValue = "-1") Integer limit,
			Authentication authentication
	) {
		
		UserType userType;
		List<Exam> exams;
		
		if(
				authentication.getAuthorities()
						.stream()
						.anyMatch(authority -> authority.getAuthority().equals("ROLE_TEACHER"))
		) {
			
			userType = UserType.TEACHER;
			
		} else {
			
			userType = UserType.STUDENT;
			
		}
		
		if(limit <= 0) {
			
			exams = examService.searchExam(
					authentication.getName(),
					userType,
					name
			);
			
		} else {
			
			exams = examService.searchExam(
					authentication.getName(),
					userType,
					name,
					limit
			);
			
		}
		
		return ResponseEntity.ok(exams);
		
	}
	
	@GetMapping("{examId}")
	@PreAuthorize("@examService.canAccess(authentication.name, #examId, authentication.authorities) == true")
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
	@PreAuthorize("@examService.canAccess(authentication.name, #examId, authentication.authorities) == true")
	public ResponseEntity<?> updateExam(
			@PathVariable Long examId,
			@RequestBody Exam exam
	) {
		
		return ResponseEntity.ok(examService.updateExam(exam, examId));
		
	}
	
}

package com.ramsey.betterexamsrestapi.resource;

import com.ramsey.betterexamsrestapi.entity.Exam;
import com.ramsey.betterexamsrestapi.entity.ExamResult;
import com.ramsey.betterexamsrestapi.entity.User;
import com.ramsey.betterexamsrestapi.service.business.ExamResultService;
import com.ramsey.betterexamsrestapi.service.business.ExamService;
import com.ramsey.betterexamsrestapi.service.business.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("teachers")
public class TeacherResource {
	
	private final TeacherService teacherService;
	private final ExamResultService examResultService;
	private final ExamService examService;
	
	@GetMapping
	@PreAuthorize("hasRole('STUDENT')")
	public ResponseEntity<?> searchTeachers(
			@RequestParam(required = false, defaultValue = "-1") Integer limit,
			@RequestParam String name
	) {
		
		List<User> teachers;
		
		if(limit <= 0) {
			
			teachers = teacherService.search(name);
			
		} else {
			
			teachers = teacherService.search(name, limit);
			
		}
		
		return ResponseEntity.ok(teachers);
		
	}
	
	@GetMapping("{username}")
	@PreAuthorize("hasRole('STUDENT') or authentication.name == #username")
	public ResponseEntity<?> getTeacher(
			@PathVariable String username
	) {
		
		return ResponseEntity.ok(teacherService.get(username));
		
	}
	
	@PostMapping("{username}/students")
	@PreAuthorize("hasRole('STUDENT')")
	public ResponseEntity<?> addStudent(
			@PathVariable String username,
			Authentication authentication
	) {
		
		teacherService.addStudentToTeacher(
				authentication.getName(),
				username
		);
		return ResponseEntity.noContent().build();
		
	}
	
	@GetMapping("{username}/results")
	@PreAuthorize("hasRole('TEACHER') and authentication.name == #username")
	public ResponseEntity<?> getTeacherResults(
			@PathVariable String username,
			@RequestParam(required = false, defaultValue = "-1") Integer limit
	) {
		
		List<ExamResult> results;
		
		if(limit <= 0) {
			
			results = examResultService.teacherResults(username);
			
		} else {
			
			results = examResultService.teacherResults(username, limit);
			
		}
		
		return ResponseEntity.ok(results);
		
	}
	
	@GetMapping("{username}/exams")
	@PreAuthorize("(hasRole('TEACHER') and authentication.name == #username) or " +
			"(hasRole('STUDENT') and @teacherService.canStudentAccess(authentication.name, #username))")
	public ResponseEntity<?> getTeacherExams(
			@PathVariable String username,
			@RequestParam(required = false, defaultValue = "-1") Integer limit
	) {
		
		List<Exam> exams;
		
		if(limit <= 0) {
			
			exams = examService.teacherExams(username);
			
		} else {
			
			exams = examService.teacherExams(username, limit);
			
		}
		
		return ResponseEntity.ok(exams);
		
	}
	
}

package com.ramsey.betterexamsrestapi.resource;

import com.ramsey.betterexamsrestapi.entity.ExamResult;
import com.ramsey.betterexamsrestapi.entity.User;
import com.ramsey.betterexamsrestapi.entity.UserType;
import com.ramsey.betterexamsrestapi.service.business.ExamResultService;
import com.ramsey.betterexamsrestapi.service.business.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("students")
public class StudentResource {
	
	private final StudentService studentService;
	private final ExamResultService examResultService;
	
	@GetMapping
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> searchStudents(
			@RequestParam(required = false, defaultValue = "-1") Integer limit,
			@RequestParam(required = false, defaultValue = "") String name,
			Authentication authentication
	) {
		
		List<User> students;
		
		if(limit <= 0) {
			
			students = studentService.searchStudentsFor(authentication.getName(), name);
			
		} else {
			
			students = studentService.searchStudentsFor(authentication.getName(), name, limit);
			
		}
		
		return ResponseEntity.ok(students);
		
	}
	
	@GetMapping("{username}")
	@PreAuthorize("hasRole('TEACHER') or authentication.name == #username")
	public ResponseEntity<?> getStudent(
			@PathVariable String username,
			Authentication authentication
	) {
		
		User user;
		
		if(
				((User) authentication.getDetails()).getType()
						.equals(UserType.TEACHER)
		) {
			
			user = studentService.getStudentForTeacher(username, authentication.getName());
			
		} else {
			
			user = studentService.getStudent(username);
			
		}
		
		return ResponseEntity.ok(user);
		
	}
	
	@GetMapping("{username}/results")
	@PreAuthorize("hasRole('TEACHER') or (hasRole('STUDENT') and authentication.name == #username)")
	public ResponseEntity<?> getStudentResults(
			@PathVariable String username,
			@RequestParam(required = false, defaultValue = "-1") Integer limit,
			Authentication authentication
	) {
		
		List<ExamResult> results;
		User user = (User) authentication.getDetails();
		
		if(user.getType().equals(UserType.TEACHER)) {
			
			if(limit <= 0) {
				
				results = examResultService.studentResultsForTeacher(username, user.getUsername());
				
			} else {
				
				results = examResultService.studentResultsForTeacher(username, user.getUsername(), limit);
				
			}
			
		} else {
			
			if(limit <= 0) {
				
				results = examResultService.studentResults(username);
				
			} else {
				
				results = examResultService.studentResults(username, limit);
				
			}
			
		}
		
		return ResponseEntity.ok(results);
		
	}
	
}

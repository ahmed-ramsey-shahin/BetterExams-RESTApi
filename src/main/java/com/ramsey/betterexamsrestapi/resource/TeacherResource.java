package com.ramsey.betterexamsrestapi.resource;

import com.ramsey.betterexamsrestapi.entity.User;
import com.ramsey.betterexamsrestapi.pojo.Response;
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
		
		Response<?> responseBody = teacherService.get(username);
		return ResponseEntity.status(responseBody.getStatus())
				.body(responseBody);
		
	}
	
	@PostMapping("{username}/students")
	@PreAuthorize("hasRole('STUDENT')")
	public ResponseEntity<?> addStudent(
			@PathVariable String username,
			Authentication authentication
	) {
		
		Response<?> responseBody = teacherService.addStudentToTeacher(
				authentication.getName(),
				username
		);
		return ResponseEntity.status(responseBody.getStatus())
				.body(responseBody);
		
	}
	
}

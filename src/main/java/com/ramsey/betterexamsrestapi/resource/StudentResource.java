package com.ramsey.betterexamsrestapi.resource;

import com.ramsey.betterexamsrestapi.entity.User;
import com.ramsey.betterexamsrestapi.pojo.Response;
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
		
		Response<?> responseBody = new Response<>(200, students);
		return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
		
	}
	
	@GetMapping("{username}")
	@PreAuthorize("hasRole('TEACHER') or authentication.name == #username")
	public ResponseEntity<?> getStudent(
			@PathVariable String username,
			Authentication authentication
	) {
		
		Response<?> responseBody;
		
		if(
				authentication.getAuthorities()
						.stream()
						.anyMatch(
								authority -> authority.getAuthority().equals("ROLE_TEACHER")
						)
		) {
			
			responseBody = studentService.getStudentForTeacher(
					username,
					authentication.getName()
			);
			
		} else {
			
			responseBody = studentService.getStudent(username);
			
		}
		
		return ResponseEntity.status(responseBody.getStatus())
				.body(responseBody);
		
	}
	
}

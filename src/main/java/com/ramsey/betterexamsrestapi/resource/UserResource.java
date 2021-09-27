package com.ramsey.betterexamsrestapi.resource;

import com.ramsey.betterexamsrestapi.entity.User;
import com.ramsey.betterexamsrestapi.pojo.Response;
import com.ramsey.betterexamsrestapi.service.business.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserResource {
	
	private final UserService userService;
	
	@PostMapping
	public ResponseEntity<?> addUser(
			@RequestBody User user
	) {
		
		Response<?> responseBody = userService.addUser(user);
		return ResponseEntity.status(responseBody.getStatus())
				.body(responseBody);
		
	}
	
	@PutMapping("{username}")
	@PreAuthorize("hasAnyRole('TEACHER', 'STUDENT') and authentication.name == #username")
	public ResponseEntity<?> updateUser(
			@PathVariable String username,
			@RequestBody User user
	) {
		
		Response<?> responseBody = userService.updateUser(user, username);
		return ResponseEntity.status(responseBody.getStatus())
				.body(responseBody);
		
	}
	
	@DeleteMapping("{username}")
	@PreAuthorize("hasAnyRole('TEACHER', 'STUDENT') and authentication.name == #username")
	public ResponseEntity<?> deleteUser(
			@PathVariable String username
	) {
		
		Response<?> responseBody = userService.deleteUser(username);
		return ResponseEntity.status(responseBody.getStatus())
				.body(responseBody);
		
	}
	
	@GetMapping("{username}/verification")
	@PreAuthorize("hasAnyRole('TEACHER', 'STUDENT') and authentication.name == #username")
	public ResponseEntity<?> sendVerificationCode(
			@PathVariable String username
	) {
		
		Response<?> responseBody = userService.sendVerificationEmail(username);
		return ResponseEntity.status(responseBody.getStatus())
				.body(responseBody);
		
	}
	
	@PostMapping("{username}/verification")
	@PreAuthorize("hasAnyRole('TEACHER', 'STUDENT') and authentication.name == #username")
	public ResponseEntity<?> verify(
			@PathVariable String username,
			@RequestBody String verificationCode
	) {
		
		Response<?> responseBody = userService.verify(username, verificationCode);
		return ResponseEntity.status(responseBody.getStatus())
				.body(responseBody);
		
	}
	
}

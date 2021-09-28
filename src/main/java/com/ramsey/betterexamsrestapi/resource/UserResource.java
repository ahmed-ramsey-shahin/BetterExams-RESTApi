package com.ramsey.betterexamsrestapi.resource;

import com.ramsey.betterexamsrestapi.entity.User;
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
		
		return ResponseEntity.ok(userService.addUser(user));
		
	}
	
	@PutMapping("{username}")
	@PreAuthorize("hasAnyRole('TEACHER', 'STUDENT') and authentication.name == #username")
	public ResponseEntity<?> updateUser(
			@PathVariable String username,
			@RequestBody User user
	) {
		
		return ResponseEntity.ok(userService.updateUser(user, username));
		
	}
	
	@DeleteMapping("{username}")
	@PreAuthorize("hasAnyRole('TEACHER', 'STUDENT') and authentication.name == #username")
	public ResponseEntity<?> deleteUser(
			@PathVariable String username
	) {
		
		userService.deleteUser(username);
		return ResponseEntity.noContent().build();
		
	}
	
	@GetMapping("{username}/verification")
	@PreAuthorize("hasAnyRole('TEACHER', 'STUDENT') and authentication.name == #username")
	public ResponseEntity<?> sendVerificationCode(
			@PathVariable String username
	) {
		
		userService.sendVerificationEmail(username);
		return ResponseEntity.noContent().build();
		
	}
	
	@PostMapping("{username}/verification")
	@PreAuthorize("hasAnyRole('TEACHER', 'STUDENT') and authentication.name == #username")
	public ResponseEntity<?> verify(
			@PathVariable String username,
			@RequestBody String verificationCode
	) {
		
		userService.verify(username, verificationCode);
		return ResponseEntity.noContent().build();
		
	}
	
}

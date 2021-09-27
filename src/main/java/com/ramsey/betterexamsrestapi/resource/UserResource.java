package com.ramsey.betterexamsrestapi.resource;

import com.ramsey.betterexamsrestapi.entity.User;
import com.ramsey.betterexamsrestapi.error.*;
import com.ramsey.betterexamsrestapi.pojo.ErrorResponse;
import com.ramsey.betterexamsrestapi.service.business.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
		
		try {
			
			user = userService.addUser(user);
			
		} catch(UserAlreadyExistsError | PasswordFormatError err) {
			
			var status = HttpStatus.FORBIDDEN;
			return ResponseEntity.status(status)
					.body(
							new ErrorResponse(
									status.value(),
									status.getReasonPhrase(),
									err.getMessage()
							)
					);
			
			
		}
		
		return ResponseEntity.ok(user);
		
	}
	
	@PutMapping("{username}")
	@PreAuthorize("hasAnyRole('TEACHER', 'STUDENT') and authentication.name == #username")
	public ResponseEntity<?> updateUser(
			@PathVariable String username,
			@RequestBody User user
	) {
		
		try {
			
			user = userService.updateUser(user, username);
			
		} catch(UserAlreadyExistsError | PasswordFormatError err) {
			
			var status = HttpStatus.FORBIDDEN;
			return ResponseEntity.status(status)
					.body(
							new ErrorResponse(
									status.value(),
									status.getReasonPhrase(),
									err.getMessage()
							)
					);
			
			
		} catch(UserNotFoundError err) {
			
			var status = HttpStatus.NOT_FOUND;
			return ResponseEntity.status(status)
					.body(
							new ErrorResponse(
									status.value(),
									status.getReasonPhrase(),
									err.getMessage()
							)
					);
			
		}
		
		return ResponseEntity.ok(user);
		
	}
	
	@DeleteMapping("{username}")
	@PreAuthorize("hasAnyRole('TEACHER', 'STUDENT') and authentication.name == #username")
	public ResponseEntity<?> deleteUser(
			@PathVariable String username
	) {
		
		try {
			
			userService.deleteUser(username);
			
		} catch(UserNotFoundError err) {
			
			var status = HttpStatus.NOT_FOUND;
			return ResponseEntity.status(status)
					.body(
							new ErrorResponse(
									status.value(),
									status.getReasonPhrase(),
									err.getMessage()
							)
					);
			
		}
		
		return ResponseEntity.noContent().build();
		
	}
	
	@GetMapping("{username}/verification")
	@PreAuthorize("hasAnyRole('TEACHER', 'STUDENT') and authentication.name == #username")
	public ResponseEntity<?> sendVerificationCode(
			@PathVariable String username
	) {
		
		try {
			
			userService.sendVerificationEmail(username);
			
		} catch(Exception ex) {
			
			var status = HttpStatus.INTERNAL_SERVER_ERROR;
			return ResponseEntity.status(status)
					.body(
							new ErrorResponse(
									status.value(),
									status.getReasonPhrase(),
									ex.getMessage()
							)
					);
			
		}
		
		return ResponseEntity.noContent().build();
		
	}
	
	@PostMapping("{username}/verification")
	@PreAuthorize("hasAnyRole('TEACHER', 'STUDENT') and authentication.name == #username")
	public ResponseEntity<?> verify(
			@PathVariable String username,
			@RequestBody String verificationCode
	) {
		
		try {
			
			userService.verify(username, verificationCode);
			
		} catch(UserNotFoundError err) {
			
			var status = HttpStatus.NOT_FOUND;
			return ResponseEntity.status(status)
					.body(
							new ErrorResponse(
									status.value(),
									status.getReasonPhrase(),
									err.getMessage()
							)
					);
			
		} catch(UserAlreadyVerifiedError | VerificationCodeNotCorrectError err) {
			
			var status = HttpStatus.FORBIDDEN;
			return ResponseEntity.status(status)
					.body(
							new ErrorResponse(
									status.value(),
									status.getReasonPhrase(),
									err.getMessage()
							)
					);
			
		} catch(Exception ex) {
			
			var status = HttpStatus.INTERNAL_SERVER_ERROR;
			return ResponseEntity.status(status)
					.body(
							new ErrorResponse(
									status.value(),
									status.getReasonPhrase(),
									ex.getMessage()
							)
					);
			
		}
		
		return ResponseEntity.noContent().build();
		
	}
	
}

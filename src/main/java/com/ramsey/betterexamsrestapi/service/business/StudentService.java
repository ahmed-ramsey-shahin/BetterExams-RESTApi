package com.ramsey.betterexamsrestapi.service.business;

import com.ramsey.betterexamsrestapi.entity.User;
import com.ramsey.betterexamsrestapi.pojo.Response;
import com.ramsey.betterexamsrestapi.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
	
	private final UserRepo userRepo;
	
	public List<User> searchStudentsFor(String username, String studentName) {
		
		return searchStudentsFor(username, studentName, Pageable.unpaged());
		
	}
	
	public List<User> searchStudentsFor(String username, String studentName, Integer limit) {
		
		return searchStudentsFor(username, studentName, Pageable.ofSize(limit));
		
	}
	
	private List<User> searchStudentsFor(String username, String studentName, Pageable pageable) {
		
		return userRepo.searchStudentForTeacher(username, studentName, pageable);
		
	}
	
	public Response<?> getStudent(String username) {
		
		Optional<User> student = userRepo.findById(username);
		
		if(student.isEmpty()) {
			
			return new Response<>(404, "The user that you're looking for was not found");
			
		}
		
		return new Response<>(200, student.get());
		
	}
	
	public Response<?> getStudentForTeacher(String studentUsername, String teacherUsername) {
		
		Optional<User> student = userRepo.findStudentForTeacher(studentUsername, teacherUsername);
		
		if(student.isEmpty()) {
			
			return new Response<>(404, "The user that you're looking for was not found");
			
		}
		
		return new Response<>(200, student.get());
		
	}
	
}
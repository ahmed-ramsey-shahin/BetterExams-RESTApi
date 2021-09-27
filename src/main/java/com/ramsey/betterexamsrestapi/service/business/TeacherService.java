package com.ramsey.betterexamsrestapi.service.business;

import com.ramsey.betterexamsrestapi.entity.Student;
import com.ramsey.betterexamsrestapi.entity.Teacher;
import com.ramsey.betterexamsrestapi.entity.User;
import com.ramsey.betterexamsrestapi.entity.UserType;
import com.ramsey.betterexamsrestapi.pojo.Response;
import com.ramsey.betterexamsrestapi.repo.StudentRepo;
import com.ramsey.betterexamsrestapi.repo.TeacherRepo;
import com.ramsey.betterexamsrestapi.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {
	
	private final TeacherRepo teacherRepo;
	private final UserRepo userRepo;
	private final StudentRepo studentRepo;
	
	private List<User> search(String name, Pageable pageable) {
		
		return userRepo.findByName(name, UserType.TEACHER, pageable);
		
	}
	
	public List<User> search(String name, Integer limit) {
		
		return search(name, Pageable.ofSize(limit));
		
	}
	
	public List<User> search(String name) {
		
		return search(name, Pageable.unpaged());
		
	}
	
	public Response<?> get(String username) {
		
		Optional<User> teacher = userRepo.findByUsernameAndType(
				username,
				UserType.TEACHER
		);
		
		if(teacher.isEmpty()) {
			
			return new Response<>(404, "The user that you're looking for was not found");
			
		}
		
		return new Response<>(200, teacher);
		
	}
	
	public Response<?> addStudentToTeacher(String studentUsername, String teacherUsername) {
		
		Optional<Teacher> teacher = teacherRepo.findByUsername(teacherUsername);
		Optional<Student> student = studentRepo.findByUsername(studentUsername);
		
		if(teacher.isEmpty() || student.isEmpty()) {
			
			return new Response<>(404, "The user that you're looking for was not found");
			
		}
		
		teacher.get().getStudents().add(student.get());
		teacherRepo.save(teacher.get());
		return new Response<>(200, "Student added successfully");
		
	}
	
}

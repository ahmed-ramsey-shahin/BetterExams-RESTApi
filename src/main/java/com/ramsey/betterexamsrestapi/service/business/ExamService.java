package com.ramsey.betterexamsrestapi.service.business;

import com.ramsey.betterexamsrestapi.entity.Exam;
import com.ramsey.betterexamsrestapi.entity.Teacher;
import com.ramsey.betterexamsrestapi.entity.User;
import com.ramsey.betterexamsrestapi.entity.UserType;
import com.ramsey.betterexamsrestapi.error.ExamNotFoundError;
import com.ramsey.betterexamsrestapi.error.ExamNotSavedError;
import com.ramsey.betterexamsrestapi.error.UserNotFoundError;
import com.ramsey.betterexamsrestapi.repo.ExamRepo;
import com.ramsey.betterexamsrestapi.repo.TeacherRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamService {
	
	private final UserService userService;
	private final TeacherService teacherService;
	private final ExamRepo examRepo;
	private final TeacherRepo teacherRepo;
	
	public Exam addExam(Exam newExam, String username) {
		
		User user = userService.loadUserByUsername(username);
		Teacher teacher = teacherService.get(user);
		
		if(
				teacher.getExams().stream()
						.anyMatch(exam -> exam.getName().equalsIgnoreCase(newExam.getName()))
		) {
			
			throw new ExamNotSavedError("You have created this exam before");
			
		}
		
		examRepo.save(newExam);
		teacher.getExams().add(newExam);
		teacherRepo.save(teacher);
		return examRepo.findById(newExam.getId()).orElseThrow(ExamNotSavedError::new);
		
	}
	
	public List<Exam> searchExam(String username, UserType type, String name, Integer limit) {
		
		return searchExam(username, type, name, Pageable.ofSize(limit));
		
	}
	
	public List<Exam> searchExam(String username, UserType type, String name) {
		
		return searchExam(username, type, name, Pageable.unpaged());
		
	}
	
	private List<Exam> searchExam(String username, UserType type, String name, Pageable pageable) {
		
		List<Exam> exams = null;
		
		switch(type) {
			
			case TEACHER:
				exams = examRepo.searchExamForTeacher(username, name, pageable);
				break;
			case STUDENT:
				exams = examRepo.searchExamForStudent(username, name, pageable);
				break;
			
		}
		
		return exams;
		
	}
	
	public boolean canAccess(String username, Long examId, Collection<? extends GrantedAuthority> authorities) {
		
		if(
				authorities.stream()
						.anyMatch(authority -> authority.getAuthority().equals("ROLE_TEACHER"))
		) {
			
			return teacherRepo.findByUsername(username)
					.orElseThrow(() -> new UserNotFoundError(username))
					.getExams()
					.stream()
					.anyMatch(exam -> exam.getId().equals(examId));
			
		} else {
			
			var result = examRepo.studentCanAccess(username, examId);
			log.info(String.valueOf(result));
			return result;
			
		}
		
	}
	
	public Exam get(Long id) {
		
		Optional<Exam> exam = examRepo.findById(id);
		
		if(exam.isEmpty()) {
			
			throw new ExamNotFoundError(id);
			
		}
		
		return exam.get();
		
	}
	
}

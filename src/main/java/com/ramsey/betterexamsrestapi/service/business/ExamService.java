package com.ramsey.betterexamsrestapi.service.business;

import com.ramsey.betterexamsrestapi.entity.Exam;
import com.ramsey.betterexamsrestapi.entity.Teacher;
import com.ramsey.betterexamsrestapi.entity.User;
import com.ramsey.betterexamsrestapi.entity.UserType;
import com.ramsey.betterexamsrestapi.error.ExamNotFoundError;
import com.ramsey.betterexamsrestapi.error.UserNotFoundError;
import com.ramsey.betterexamsrestapi.repo.ExamRepo;
import com.ramsey.betterexamsrestapi.repo.TeacherRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExamService {
	
	private final UserService userService;
	private final TeacherService teacherService;
	private final ExamRepo examRepo;
	private final TeacherRepo teacherRepo;
	
	public Exam addExam(Exam newExam, String username) {
		
		User user = userService.loadUserByUsername(username);
		Teacher teacher = teacherService.get(user);
		examRepo.save(newExam);
		teacher.getExams().add(newExam);
		teacherRepo.save(teacher);
		return newExam;
		
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
	
	public boolean canAccess(User user, Long examId) {
		
		if(
				user.getType().equals(UserType.TEACHER)
		) {
			
			return teacherRepo.findByUsername(user.getUsername())
					.orElseThrow(() -> new UserNotFoundError(user.getUsername()))
					.getExams()
					.stream()
					.anyMatch(exam -> exam.getId().equals(examId));
			
		} else {
			
			return examRepo.studentCanAccess(user.getUsername(), examId);
			
		}
		
	}
	
	public Exam get(Long id) {
		
		Optional<Exam> exam = examRepo.findById(id);
		
		if(exam.isEmpty()) {
			
			throw new ExamNotFoundError(id);
			
		}
		
		return exam.get();
		
	}
	
	public Exam updateExam(Exam newExam, Long examId) {
		
		Exam exam = get(examId);
		exam.setQuestions(newExam.getQuestions());
		exam.setTimeInMinutes(newExam.getTimeInMinutes());
		exam.setName(newExam.getName());
		exam.setRequiredScore(newExam.getRequiredScore());
		examRepo.save(exam);
		return exam;
		
	}
	
}

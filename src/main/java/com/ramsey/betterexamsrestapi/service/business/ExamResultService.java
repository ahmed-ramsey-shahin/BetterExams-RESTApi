package com.ramsey.betterexamsrestapi.service.business;

import com.ramsey.betterexamsrestapi.entity.ExamResult;
import com.ramsey.betterexamsrestapi.entity.Student;
import com.ramsey.betterexamsrestapi.entity.UserType;
import com.ramsey.betterexamsrestapi.error.UserNotFoundError;
import com.ramsey.betterexamsrestapi.repo.ExamResultRepo;
import com.ramsey.betterexamsrestapi.repo.StudentRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExamResultService {
	
	private final ExamResultRepo examResultRepo;
	private final StudentRepo studentRepo;
	
	public ExamResult addExamResult(ExamResult examResult, String username) {
		
		Optional<Student> student = studentRepo.findByUsername(username);
		
		if(student.isEmpty()) {
			
			throw new UserNotFoundError(username);
			
		}
		
		examResultRepo.save(examResult);
		student.get().getResults().add(examResult);
		studentRepo.save(student.get());
		return examResult;
		
	}
	
	public List<ExamResult> searchExams(String username, UserType type) {
		
		return searchExams(username, type, Pageable.unpaged());
		
	}
	
	public List<ExamResult> searchExams(String username, UserType type, Integer limit) {
		
		return searchExams(username, type, Pageable.ofSize(limit));
		
	}
	
	private List<ExamResult> searchExams(String username, UserType type, Pageable pageable) {
		
		List<ExamResult> examResults;
		
		if(type.equals(UserType.TEACHER)) {
			
			examResults = examResultRepo.findForTeacher(username, pageable);
			
		} else {
			
			examResults = examResultRepo.findForStudent(username, pageable);
			
		}
		
		return examResults;
		
	}
	
}

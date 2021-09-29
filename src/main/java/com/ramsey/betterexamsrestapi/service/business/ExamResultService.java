package com.ramsey.betterexamsrestapi.service.business;

import com.ramsey.betterexamsrestapi.entity.ExamResult;
import com.ramsey.betterexamsrestapi.entity.Student;
import com.ramsey.betterexamsrestapi.entity.User;
import com.ramsey.betterexamsrestapi.entity.UserType;
import com.ramsey.betterexamsrestapi.error.ExamNotFoundError;
import com.ramsey.betterexamsrestapi.error.UserNotFoundError;
import com.ramsey.betterexamsrestapi.repo.ExamResultRepo;
import com.ramsey.betterexamsrestapi.repo.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
	
	public boolean canAccess(User user, Long resultId) {
		
		if(user.getType().equals(UserType.STUDENT)) {
			
			return examResultRepo.studentCanAccess(user.getUsername(), resultId);
			
		} else {
			
			return examResultRepo.teacherCanAccess(user.getUsername(), resultId);
			
		}
		
	}
	
	public ExamResult get(Long resultId) {
		
		Optional<ExamResult> examResult = examResultRepo.findById(resultId);
		
		if(examResult.isEmpty()) {
			
			throw new ExamNotFoundError(resultId);
			
		}
		
		return examResult.get();
		
	}
	
	public List<ExamResult> studentResults(String username, Integer limit) {
		
		return studentResults(username, Pageable.ofSize(limit));
		
	}
	
	public List<ExamResult> studentResults(String username) {
		
		return studentResults(username, Pageable.unpaged());
		
	}
	
	private List<ExamResult> studentResults(String username, Pageable pageable) {
		
		return examResultRepo.studentResults(username, pageable);
		
	}
	
	public List<ExamResult> teacherResults(String username) {
		
		return teacherResults(username, Pageable.unpaged());
		
	}
	
	public List<ExamResult> teacherResults(String username, Integer limit) {
		
		return teacherResults(username, Pageable.ofSize(limit));
		
	}
	
	private List<ExamResult> teacherResults(String username, Pageable pageable) {
		
		return examResultRepo.teacherResults(username, pageable);
		
	}
	
	public List<ExamResult> examResults(Long examId) {
		
		return examResults(examId, Pageable.unpaged());
		
	}
	
	public List<ExamResult> examResults(Long examId, Integer limit) {
		
		return examResults(examId, Pageable.ofSize(limit));
		
	}
	
	private List<ExamResult> examResults(Long examId, Pageable pageable) {
		
		return examResultRepo.examResults(examId, pageable);
		
	}
	
	public List<ExamResult> studentResultsForTeacher(String studentUsername, String teacherUsername) {
		
		return studentResultsForTeacher(studentUsername, teacherUsername, Pageable.unpaged());
		
	}
	
	public List<ExamResult> studentResultsForTeacher(String studentUsername, String teacherUsername, Integer limit) {
		
		return studentResultsForTeacher(studentUsername, teacherUsername, Pageable.ofSize(limit));
		
	}
	
	private List<ExamResult> studentResultsForTeacher(String studentUsername, String teacherUsername, Pageable pageable) {
		
		return examResultRepo.studentResultsForTeacher(studentUsername, teacherUsername, pageable);
		
	}
	
}

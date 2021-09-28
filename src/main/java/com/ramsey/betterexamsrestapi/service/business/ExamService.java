package com.ramsey.betterexamsrestapi.service.business;

import com.ramsey.betterexamsrestapi.entity.Exam;
import com.ramsey.betterexamsrestapi.entity.Teacher;
import com.ramsey.betterexamsrestapi.entity.User;
import com.ramsey.betterexamsrestapi.error.ExamNotSavedError;
import com.ramsey.betterexamsrestapi.repo.ExamRepo;
import com.ramsey.betterexamsrestapi.repo.TeacherRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
	
}

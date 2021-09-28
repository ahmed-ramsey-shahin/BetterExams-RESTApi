package com.ramsey.betterexamsrestapi.repo;

import com.ramsey.betterexamsrestapi.entity.Exam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepo extends CrudRepository<Exam, Long> {
	
	@Query("SELECT e FROM Teacher t, IN(t.students) s, IN(t.exams) e WHERE s.user.username = :username AND e.name LIKE %:examName% ORDER BY e.id")
	List<Exam> searchExamForStudent(String username, String examName, Pageable pageable);
	@Query("SELECT e FROM Teacher t, IN(t.exams) e WHERE t.user.username = :username AND e.name LIKE %:examName% ORDER BY e.id")
	List<Exam> searchExamForTeacher(String username, String examName, Pageable pageable);
	
}

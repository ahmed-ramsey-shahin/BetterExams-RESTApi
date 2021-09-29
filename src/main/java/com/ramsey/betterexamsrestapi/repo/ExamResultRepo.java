package com.ramsey.betterexamsrestapi.repo;

import com.ramsey.betterexamsrestapi.entity.ExamResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamResultRepo extends CrudRepository<ExamResult, Long> {
	
	@Query("SELECT r " +
			"FROM Teacher t, IN(t.students) s, IN(s.results) r " +
			"WHERE t.user.username = :username")
	List<ExamResult> findForTeacher(String username, Pageable pageable);
	@Query("SELECT r " +
			"FROM Teacher t, IN(t.students) s, IN(s.results) r " +
			"WHERE s.user.username = :username")
	List<ExamResult> findForStudent(String username, Pageable pageable);
	
}

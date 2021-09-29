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
	@Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END " +
			"FROM Student s, IN(s.results) r " +
			"WHERE s.user.username = :username AND r.id = :resultId")
	Boolean studentCanAccess(String username, Long resultId);
	@Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END " +
			"FROM Teacher t, IN(t.students) s, IN(s.results) r " +
			"WHERE t.user.username = :username AND r.id = :resultId")
	Boolean teacherCanAccess(String username, Long resultId);
	@Query("SELECT r FROM Student s, IN(s.results) r WHERE s.user.username = :username")
	List<ExamResult> studentResults(String username, Pageable pageable);
	@Query("SELECT r FROM ExamResult r WHERE r.exm.id = :examId")
	List<ExamResult> examResults(Long examId, Pageable pageable);
	@Query("SELECT r FROM Teacher t, IN(t.exams) e, ExamResult r WHERE t.user.username = :username AND r.exm.id = e.id")
	List<ExamResult> teacherResults(String username, Pageable pageable);
	@Query("SELECT r FROM Teacher t, IN(t.students) s, IN(s.results) r " +
			"WHERE t.user.username = :teacherUsername AND s.user.username = :studentUsername")
	List<ExamResult> studentResultsForTeacher(String studentUsername, String teacherUsername, Pageable pageable);
	
}

package com.ramsey.betterexamsrestapi.repo;

import com.ramsey.betterexamsrestapi.entity.Teacher;
import com.ramsey.betterexamsrestapi.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepo extends CrudRepository<Teacher, Long> {
	
	Optional<Teacher> findByUser(User user);

	@Query("SELECT t FROM Teacher t, IN(t.user) u WHERE u.username = :username")
	Optional<Teacher> findByUsername(String username);
	@Query("SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END " +
			"FROM Teacher t, IN(t.students) s " +
			"WHERE t.user.username = :teacherUsername AND s.user.username = :username")
	Boolean canStudentAccess(String username, String teacherUsername);
	
}

package com.ramsey.betterexamsrestapi.repo;

import com.ramsey.betterexamsrestapi.entity.Teacher;
import com.ramsey.betterexamsrestapi.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepo extends CrudRepository<Teacher, Long> {
	
	@Query("SELECT u FROM User u WHERE (u.username LIKE %:name% OR u.fullName LIKE %:name%) AND u.userType = com.ramsey.betterexamsrestapi.entity.UserType.TEACHER")
	List<Teacher> findByName(String name, Pageable pageable);
	Optional<Teacher> findByUser(User user);
	@Query("SELECT t FROM Teacher t, IN(t.user) u WHERE u.username = :username")
	Optional<Teacher> findByUsername(String username);
	
}

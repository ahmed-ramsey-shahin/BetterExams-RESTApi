package com.ramsey.betterexamsrestapi.repo;

import com.ramsey.betterexamsrestapi.entity.User;
import com.ramsey.betterexamsrestapi.entity.UserType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<User, String> {
	
	Boolean existsByUsernameOrEmail(String username, String email);
	Boolean existsByEmail(String email);
	Optional<User> findByUsernameAndType(String username, UserType userType);
	
	@Query("SELECT u FROM User u WHERE (u.username LIKE %:name% OR u.fullName LIKE %:name%) AND u.type = :type")
	List<User> findByName(String name, UserType type, Pageable pageable);
	@Query("SELECT s.user FROM Teacher t, IN(t.students) s WHERE (t.user.username = :username) and (s.user.username LIKE %:studentName% or s.user.fullName LIKE %:studentName%)")
	List<User> searchStudentForTeacher(String username, String studentName, Pageable pageable);
	@Query("SELECT s.user FROM Teacher t, IN(t.students) s WHERE t.user.username = :teacher and s.user.username = :student")
	Optional<User> findStudentForTeacher(String student, String teacher);
	
}

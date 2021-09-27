package com.ramsey.betterexamsrestapi.repo;

import com.ramsey.betterexamsrestapi.entity.Teacher;
import com.ramsey.betterexamsrestapi.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepo extends CrudRepository<Teacher, Long> {
	
	Optional<Teacher> findByUser(User user);
	
}

package com.ramsey.betterexamsrestapi.repo;

import com.ramsey.betterexamsrestapi.entity.Student;
import com.ramsey.betterexamsrestapi.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepo extends CrudRepository<Student, Long> {
	
	Optional<Student> findByUser(User user);
	
}

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
	Optional<User> findByUsernameAndUserType(String username, UserType userType);
	
	@Query("SELECT u FROM User u WHERE (u.username LIKE %:name% OR u.fullName LIKE %:name%) AND u.userType = :userType")
	List<User> findByName(String name, UserType userType, Pageable pageable);
	
}

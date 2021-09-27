package com.ramsey.betterexamsrestapi.repo;

import com.ramsey.betterexamsrestapi.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<User, String> {  }

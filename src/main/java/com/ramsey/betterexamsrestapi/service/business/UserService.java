package com.ramsey.betterexamsrestapi.service.business;

import com.ramsey.betterexamsrestapi.entity.User;
import com.ramsey.betterexamsrestapi.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
	
	private final UserRepo userRepo;
	
	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> user = userRepo.findById(username);
		
		if(user.isEmpty()) {
			
			throw new UsernameNotFoundException(username);
			
		}
		
		return user.get();
		
	}
	
}

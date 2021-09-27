package com.ramsey.betterexamsrestapi.service.business;

import com.ramsey.betterexamsrestapi.entity.Student;
import com.ramsey.betterexamsrestapi.entity.Teacher;
import com.ramsey.betterexamsrestapi.entity.User;
import com.ramsey.betterexamsrestapi.error.*;
import com.ramsey.betterexamsrestapi.repo.StudentRepo;
import com.ramsey.betterexamsrestapi.repo.TeacherRepo;
import com.ramsey.betterexamsrestapi.repo.UserRepo;
import com.ramsey.betterexamsrestapi.service.util.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
	
	private final UserRepo userRepo;
	private final TeacherRepo teacherRepo;
	private final StudentRepo studentRepo;
	private final EmailSenderService emailSender;
	private final Jedis jedis;
	private final PasswordEncoder passwordEncoder;
	private Pattern passwordPattern;
	
	@PostConstruct
	public void init() {
		
		passwordPattern = Pattern.compile(
				"^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[!@#$%^&*()_+=-])[A-z\\d!@#$%^&*()_+=-]{8,}$"
		);
		
	}
	
	public User addUser(User user) {
		
		if(!passwordPattern.matcher(user.getPassword()).matches()) {
			
			throw new PasswordFormatError();
			
		}
		
		if(userRepo.existsByUsernameOrEmail(user.getUsername(), user.getEmail())) {
			
			throw new UserAlreadyExistsError();
			
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		switch(user.getType()) {
			
			case TEACHER:
				user.setAuthorities(List.of(new SimpleGrantedAuthority("ROLE_TEACHER")));
				teacherRepo.save(new Teacher(user));
				break;
			case STUDENT:
				user.setAuthorities(List.of(new SimpleGrantedAuthority("ROLE_STUDENT")));
				studentRepo.save(new Student(user));
				break;
			
		}
		
		sendVerificationEmail(user.getUsername());
		return user;
		
	}
	
	public void sendVerificationEmail(String username) {
		
		Optional<User> user = userRepo.findById(username);
		
		if(user.isEmpty()) {
			
			throw new UsernameNotFoundException(username);
			
		}
		
		if(user.get().isEnabled()) {
			
			throw new UserAlreadyVerifiedError(username);
			
		}
		
		String code = emailSender.sendConfirmationEmail(user.get().getEmail());
		jedis.setex(user.get().getUsername(), (60L * 60L), code);
		
	}
	
	public void verify(String username, String code) {
		
		Optional<User> user = userRepo.findById(username);
		
		if(user.isEmpty()) {
			
			throw new UserNotFoundError(username);
			
		}
		
		if(!user.get().isEnabled()) {
			
			if(jedis.exists(username)) {
				
				if(jedis.get(username).equals(code)) {
					
					user.get().setEnabled(true);
					userRepo.save(user.get());
					jedis.del(username);
					return;
					
				}
				
				throw new VerificationCodeNotCorrectError();
				
			}
			
			throw new VerificationCodeNotCorrectError();
			
		}
		
		throw new UserAlreadyVerifiedError(username);
		
	}
	
	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> user = userRepo.findById(username);
		
		if(user.isEmpty()) {
			
			throw new UsernameNotFoundException(username);
			
		}
		
		return user.get();
		
	}
	
	public User updateUser(User newUser, String username) {
		
		Optional<User> user = userRepo.findById(username);
		boolean emailChanged = false;
		
		if(user.isEmpty()) {
			
			throw new UserNotFoundError(username);
			
		}
		
		user.get().setFullName(newUser.getFullName());
		user.get().setEnabled(false);
		
		if(!user.get().getEmail().equals(newUser.getEmail())) {
			
			emailChanged = true;
			
			if(userRepo.existsByEmail(newUser.getEmail())) {
				
				throw new UserAlreadyExistsError();
				
			} else {
				
				user.get().setEmail(newUser.getEmail());
				user.get().setEnabled(false);
				
			}
			
		}
		
		if(newUser.getPassword() != null && !user.get().getPassword().equals(newUser.getPassword())) {
			
			if(!passwordPattern.matcher(newUser.getPassword()).matches()) {
				
				throw new PasswordFormatError();
				
			}
			
			user.get().setPassword(passwordEncoder.encode(newUser.getPassword()));
			
		}
		
		userRepo.save(user.get());
		
		if(emailChanged) {
			
			sendVerificationEmail(username);
			
		}
		
		return user.get();
		
	}
	
	public void deleteUser(String username) {
		
		Optional<User> user = userRepo.findById(username);
		
		if(user.isEmpty()) {
			
			throw new UserNotFoundError(username);
			
		}
		
		switch(user.get().getType()) {
			
			case TEACHER:
				Optional<Teacher> teacher = teacherRepo.findByUser(user.get());
				teacher.ifPresent(teacherRepo::delete);
				break;
			case STUDENT:
				Optional<Student> student = studentRepo.findByUser(user.get());
				student.ifPresent(studentRepo::delete);
				break;
			
		}
		
	}
	
}

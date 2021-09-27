package com.ramsey.betterexamsrestapi.service.business;

import com.ramsey.betterexamsrestapi.entity.Student;
import com.ramsey.betterexamsrestapi.entity.Teacher;
import com.ramsey.betterexamsrestapi.entity.User;
import com.ramsey.betterexamsrestapi.pojo.Response;
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
	
	public Response<?> addUser(User user) {
		
		Response<String> forbiddenResponse = new Response<>();
		forbiddenResponse.setStatus(403);
		
		if(!passwordPattern.matcher(user.getPassword()).matches()) {
			
			forbiddenResponse.setMessage("Invalid password");
			return forbiddenResponse;
			
		}
		
		if(userRepo.existsByUsernameOrEmail(user.getUsername(), user.getEmail())) {
			
			forbiddenResponse.setMessage("Username, or email already exists");
			return forbiddenResponse;
			
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		switch(user.getUserType()) {
			
			case TEACHER:
				user.setAuthorities(List.of(new SimpleGrantedAuthority("ROLE_TEACHER")));
				teacherRepo.save(new Teacher(user));
				break;
			case STUDENT:
				user.setAuthorities(List.of(new SimpleGrantedAuthority("ROLE_STUDENT")));
				studentRepo.save(new Student(user));
				break;
			
		}
		
		Response<?> verificationEmailResponse = sendVerificationEmail(user.getUsername());
		
		if(!verificationEmailResponse.getStatus().equals(200)) {
			
			return verificationEmailResponse;
			
		}
		
		return new Response<>(200, user);
		
	}
	
	public Response<?> sendVerificationEmail(String username) {
		
		Optional<User> user = userRepo.findById(username);
		
		if(user.isEmpty()) {
			
			return new Response<>(404, "The user that you're looking for was not found");
			
		}
		
		if(user.get().isEnabled()) {
			
			return new Response<>(
					403,
					"The user is already verified"
			);
			
		}
		
		String code = emailSender.sendConfirmationEmail(user.get().getEmail());
		jedis.setex(user.get().getUsername(), (60L * 60L), code);
		return new Response<>(200, "Verification code sent successfully");
		
	}
	
	public Response<?> verify(String username, String code) {
		
		Optional<User> user = userRepo.findById(username);
		
		if(user.isEmpty()) {
			
			return new Response<>(404, "The user you're looking for was not found");
			
		}
		
		if(!user.get().isEnabled()) {
			
			if(jedis.exists(username)) {
				
				if(jedis.get(username).equals(code)) {
					
					user.get().setEnabled(true);
					userRepo.save(user.get());
					jedis.del(username);
					return new Response<>(200, "User verified successfully");
					
				}
				
				return new Response<>(403, "The verification code is not correct");
				
			}
			
			return new Response<>(403, "There is no verification code for this user");
			
		}
		
		return new Response<>(403, "The user is already verified");
		
	}
	
	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> user = userRepo.findById(username);
		
		if(user.isEmpty()) {
			
			throw new UsernameNotFoundException(username);
			
		}
		
		return user.get();
		
	}
	
	public Response<?> updateUser(User newUser, String username) {
		
		Optional<User> user = userRepo.findById(username);
		boolean emailChanged = false;
		
		if(user.isEmpty()) {
			
			return new Response<>(404, "The user you're looking for was not found");
			
		}
		
		user.get().setFullName(newUser.getFullName());
		user.get().setEnabled(false);
		
		if(!user.get().getEmail().equals(newUser.getEmail())) {
			
			emailChanged = true;
			
			if(userRepo.existsByEmail(newUser.getEmail())) {
				
				return new Response<>(403, "Email already exists");
				
			} else {
				
				user.get().setEmail(newUser.getEmail());
				user.get().setEnabled(false);
				
			}
			
		}
		
		if(newUser.getPassword() != null && !user.get().getPassword().equals(newUser.getPassword())) {
			
			if(!passwordPattern.matcher(newUser.getPassword()).matches()) {
				
				return new Response<>(403, "Invalid password");
				
			}
			
			user.get().setPassword(passwordEncoder.encode(newUser.getPassword()));
			
		}
		
		userRepo.save(user.get());
		
		if(emailChanged) {
			
			sendVerificationEmail(username);
			
		}
		
		return new Response<>(200, user.get());
		
	}
	
	public Response<?> deleteUser(String username) {
		
		Optional<User> user = userRepo.findById(username);
		
		if(user.isEmpty()) {
			
			return new Response<>(404, "The user you're looking for was not found");
			
		}
		
		switch(user.get().getUserType()) {
			
			case TEACHER:
				Optional<Teacher> teacher = teacherRepo.findByUser(user.get());
				teacher.ifPresent(teacherRepo::delete);
				break;
			case STUDENT:
				Optional<Student> student = studentRepo.findByUser(user.get());
				student.ifPresent(studentRepo::delete);
				break;
			
		}
		
		return new Response<>(200, "User deleted successfully");
		
	}
	
}

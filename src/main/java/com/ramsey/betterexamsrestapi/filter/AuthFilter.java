package com.ramsey.betterexamsrestapi.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramsey.betterexamsrestapi.entity.User;
import com.ramsey.betterexamsrestapi.pojo.ErrorResponse;
import com.ramsey.betterexamsrestapi.service.business.UserService;
import com.ramsey.betterexamsrestapi.service.util.DigestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SuppressWarnings("RegExpRedundantEscape")
@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
	
	private final UserService userService;
	private final DigestService digestService;
	
	private static long differenceBetweenTwoDatesInMinutes(Date date1, Date date2) {
		
		long diffInMils = date1.getTime() - date2.getTime();
		return TimeUnit.MINUTES.convert(diffInMils, TimeUnit.MILLISECONDS);
		
	}
	
	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain
	) throws ServletException, IOException {
		
		if(request.getRequestURI().matches("^.*(users)(|\\/)$")) {
			
			if(request.getMethod().equalsIgnoreCase("post")) {
				
				filterChain.doFilter(request, response);
				return;
				
			}
			
		}
		
		String authHeader = request.getHeader(AUTHORIZATION);
		ErrorResponse errorResponse = new ErrorResponse();
		var status = HttpStatus.FORBIDDEN;
		errorResponse.setStatus(status.value());
		
		try {
			
			StringTokenizer tokenizer = new StringTokenizer(authHeader);
			String authMethod = tokenizer.nextToken();
			
			if(authMethod.equalsIgnoreCase("Digest")) {
				
				Map<String, String> tokens = new HashMap<>();
				
				while (tokenizer.hasMoreElements()) {
					
					String[] arr = tokenizer.nextToken(",").trim().split("=", 2);
					tokens.put(arr[0], arr[1]);
					
				}
				
				String username = tokens.get("username");
				String nonce = tokens.get("nonce");
				String dateString = tokens.get("date");
				String hmac = tokens.get("hmac");
				String realm = request.getServerName();
				String method = request.getMethod().toLowerCase(Locale.ROOT);
				String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
				String resource = request.getRequestURI();
				User user = userService.loadUserByUsername(username);
				String pattern = "dd-MM-yyyy hh:mm:ss";
				SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
				Date date = dateFormat.parse(dateString);
				long diff = differenceBetweenTwoDatesInMinutes(new Date(), date);
				
				if(diff >= 0 && diff <= 30) {
					
					String generatedHmac = digestService.digestRequestGenerator(
							username,
							realm,
							user.getPassword(),
							nonce,
							dateString,
							method,
							body,
							resource
					);
					
					if(user.isEnabled() || request.getRequestURI().matches("^.*(verification)(|\\/)$")) {
						
						if(generatedHmac.equals(hmac)) {
							
							SecurityContext context = SecurityContextHolder.getContext();
							UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
									user.getUsername(),
									user.getPassword(),
									user.getAuthorities()
							);
							context.setAuthentication(auth);
							SecurityContextHolder.setContext(context);
							filterChain.doFilter(request, response);
							return;
							
						}
						
						errorResponse.setMessage("Invalid hmac");
						
					} else {
						
						errorResponse.setMessage("User is not enabled");
						
					}
					
				} else {
					
					errorResponse.setMessage("Invalid date");
				
				}
				
			} else {
				
				errorResponse.setMessage("Auth method is not supported");
				
			}
			
		} catch(NullPointerException ex) {
			
			errorResponse.setMessage("Invalid authentication header");
			
		} catch(UsernameNotFoundException ex) {
			
			errorResponse.setMessage("Invalid username");
			
		} catch(ParseException ex) {
			
			errorResponse.setMessage("Invalid date format");
			
		}
		
		errorResponse.setError(status.getReasonPhrase());
		response.setContentType("application/json");
		response.setStatus(errorResponse.getStatus());
		new ObjectMapper().writeValue(response.getWriter(), errorResponse);
		
	}
	
}

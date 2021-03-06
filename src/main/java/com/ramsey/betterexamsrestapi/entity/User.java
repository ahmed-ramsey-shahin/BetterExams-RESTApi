package com.ramsey.betterexamsrestapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@JsonIgnoreProperties(value = "password", allowSetters = true)
public class User implements UserDetails {
	
	@Id
	@NonNull
	@Column(unique = true)
	@Pattern(regexp = "^(?=.*[A-z])(?=.*[\\d])[A-z\\d]{4,}$")
	private String username;
	
	@NotNull
	private String password;
	
	@NonNull
	@Column(unique = true)
	@SuppressWarnings({"RegExpRedundantEscape", "RegExpUnnecessaryNonCapturingGroup"})
	@Pattern(regexp = "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
			"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])" +
			"*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2" +
			"(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])" +
			"|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b" +
			"\\x0c\\x0e-\\x7f])+)\\])$")
	private String email;
	
	@NotNull
	@Pattern(regexp = "(?=.*[A-z])[A-z\\s]{6,}")
	private String fullName;
	
	@JsonIgnore
	private boolean enabled;
	
	@JsonIgnore
	@ElementCollection(fetch = FetchType.EAGER)
	private List<SimpleGrantedAuthority> authorities;
	
	@Enumerated(value = EnumType.STRING)
	private UserType type;
	
	{
		
		enabled = false;
		
	}
	
	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		
		return true;
		
	}
	
	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		
		return true;
		
	}
	
	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		
		return true;
		
	}
	
}

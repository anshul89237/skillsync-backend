package com.lpu.AuthService.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.lpu.AuthService.entity.User;

public class AuthUserDetails implements UserDetails{
	
	 private User user;
	 public  AuthUserDetails(User user){
		  this.user=user;
	  }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		  String role = user.getRole();

		    if (role == null || role.isBlank()) {
		        role = "ROLE_USER"; // default role
		    }
		return Collections.singleton(new SimpleGrantedAuthority(role));
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

}

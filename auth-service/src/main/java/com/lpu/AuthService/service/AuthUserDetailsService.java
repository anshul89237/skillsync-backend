package com.lpu.AuthService.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lpu.AuthService.custom.exceptions.UserNotFoundException;
import com.lpu.AuthService.entity.User;
import com.lpu.AuthService.repository.AuthUserRepository;

@Service
public class AuthUserDetailsService implements UserDetailsService{

	   @Autowired
	   private AuthUserRepository repo;

	   @Override
	   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		  
		    User au=repo.findByEmail(username).orElseThrow(()->new UserNotFoundException("USER NOT REGISTRED"));
		    return new AuthUserDetails(au);
	   }
}

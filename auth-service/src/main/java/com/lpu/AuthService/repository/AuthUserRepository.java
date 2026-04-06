package com.lpu.AuthService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lpu.AuthService.entity.User;


@Repository
public interface AuthUserRepository extends JpaRepository<User, Long>{

//    @Query("SELECT u FROM Users u WHERE u.email = :email")
//    public Users findUserByEmail(@Param("email") String email);
    
        Optional<User> findByEmail(String email);

		boolean existsByEmail(String email);
}

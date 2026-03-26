package com.lpu.AuthService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lpu.AuthService.entity.Users;


@Repository
public interface UserRepository extends JpaRepository<Users, Long>{

    @Query("SELECT u FROM Users u WHERE u.email = :email")
    public Users findUserByEmail(@Param("email") String email);
}

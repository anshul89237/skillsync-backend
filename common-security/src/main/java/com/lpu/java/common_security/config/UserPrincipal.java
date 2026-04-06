package com.lpu.java.common_security.config;

public class UserPrincipal {

    private Long userId;
    private String email;
    private String role;
    private String name;

    public UserPrincipal(Long userId, String email, String role, String name) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.name = name;
    }

    public Long getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getName() { return name; }
}

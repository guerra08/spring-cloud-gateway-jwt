package com.example.auth;

import lombok.Data;

import java.util.UUID;

@Data
public class User {

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.id = UUID.randomUUID();
    }

    private UUID id;
    private String email;
    private String password;

}

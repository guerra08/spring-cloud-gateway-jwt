package com.example.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        return UserStore.getUserByEmailAndPassword(authRequest.email(), authRequest.password())
                .map(user -> {
                    var token = JwtUtil.buildJwt(user);
                    return ResponseEntity.ok(token);
                }).orElse(ResponseEntity.status(401).build());

    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest authRequest) {
        var user = new User(authRequest.email(), authRequest.password());
        UserStore.addUser(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify")
    public ResponseEntity<Object> verify(@RequestParam String token) {
        var isValid = JwtUtil.validateToken(token);
        return isValid ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

}

package com.example.brushandcoloursBackend.auth;

import com.example.brushandcoloursBackend.auth.dto.LoginRequest;
import com.example.brushandcoloursBackend.auth.dto.LoginResponse;
import com.example.brushandcoloursBackend.user.dto.RegisterRequest;
import com.example.brushandcoloursBackend.user.dto.RegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/{register}")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request){
        RegisterResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<String> me(Authentication authentication){
        return ResponseEntity.ok(authentication.getName());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
            ){
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}

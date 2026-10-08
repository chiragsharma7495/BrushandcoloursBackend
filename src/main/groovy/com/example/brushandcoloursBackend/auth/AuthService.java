package com.example.brushandcoloursBackend.auth;

import com.example.brushandcoloursBackend.auth.dto.LoginRequest;
import com.example.brushandcoloursBackend.auth.dto.LoginResponse;
import com.example.brushandcoloursBackend.exception.EmailAlreadyExistException;
import com.example.brushandcoloursBackend.user.AppUser;
import com.example.brushandcoloursBackend.user.Role;
import com.example.brushandcoloursBackend.user.UserRepository;
import com.example.brushandcoloursBackend.user.dto.RegisterRequest;
import com.example.brushandcoloursBackend.user.dto.RegisterResponse;
import com.example.brushandcoloursBackend.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public RegisterResponse register(RegisterRequest request){
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);

        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyExistException("Email is already registered" + request.getEmail());
        }

        AppUser user = userMapper.toEntity(request);

        user.setEmail(email);

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        user.setPassword(encodedPassword);

        user.setRole(Role.USER);

        AppUser savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request){
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);

        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken
                        .unauthenticated(email, request.getPassword())
        );

        String token = jwtService.generateToken(authentication);

        return new LoginResponse(token, "Bearer", 3600);
    }

}

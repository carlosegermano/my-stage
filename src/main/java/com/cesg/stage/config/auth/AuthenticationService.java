package com.cesg.stage.config.auth;

import com.cesg.stage.config.service.JwtService;
import com.cesg.stage.exceptions.AuthenticationException;
import com.cesg.stage.exceptions.DuplicatedUserException;
import com.cesg.stage.model.User;
import com.cesg.stage.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var saved = this.userRepository.findByEmail(request.getEmail());
        if (!saved.isEmpty()) {
            throw new DuplicatedUserException("Já existe um usuário com este e-mail!");
        }
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(this.passwordEncoder.encode(request.getPassword()))
                .build();
        this.userRepository.save(user);
        var jwtToken = this.jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
        try {
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var user = this.userRepository.findByEmail(request.getEmail())
                    .orElseThrow();
            var jwtToken = this.jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(jwtToken).build();
        } catch (RuntimeException e) {
            throw new AuthenticationException("Login ou senha inválidos");
        }
    }
}

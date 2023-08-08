package com.Messenger.Messenger.service;

import com.Messenger.Messenger.domain.*;
import com.Messenger.Messenger.repository.UserRepository;
import com.Messenger.Messenger.request.AuthenticationRequest;
import com.Messenger.Messenger.request.RegisterRequest;
import com.Messenger.Messenger.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(RegisterRequest request) {
        var password = passwordEncoder.encode(request.getPassword());
        var user = User.builder()
                .email(request.getEmail())
                .password(password)
                .prevPassword(password)
                .build();
        userService.saveUser(user);
        var currentUser = userService.findUserByEmail(user.getEmail());
        userService.addRoleToUser(currentUser, request.getRole());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("OK")
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            var user = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            var authenticatedUser = (UserDetails) user.getPrincipal();
            var jwtToken = jwtService.generateToken(authenticatedUser);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .message("OK")
                    .build();
        } catch (BadCredentialsException ex) {
            // Handle incorrect password case
            return AuthenticationResponse.builder()
                    .message("Incorrect email or password : " + ex.getMessage())
                    .build();
        }
    }


}

package com.Messenger.Messenger.service;

import com.Messenger.Messenger.domain.*;
import com.Messenger.Messenger.repository.UserRepository;
import com.Messenger.Messenger.request.AuthenticationRequest;
import com.Messenger.Messenger.request.RegisterRequest;
import com.Messenger.Messenger.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .passwordConfirm(passwordEncoder.encode(request.getPasswordConfirm()))
                .build();
        userService.saveUser(user);
        var currentUser = userService.findUserByEmail(user.getEmail());
        userService.addRoleToUser(currentUser, request.getRole());
        switch (request.getRole()){
            case "ROLE_STUDENT":
                Group group = groupRepository.findById(1L).orElseThrow();
                var student = Student.builder()
                        .user(currentUser)
                        .group(group)
                        .build();
                studentRepository.save(student);
                Student studentToFind = studentRepository.findByUser(student.getUser()).orElse(null);
                if(studentToFind == null){
                    return AuthenticationResponse.builder()
                            .message("cant find student")
                            .token("")
                            .build();
                }

                Set<Student> students = new HashSet<>(group.getStudents());
                students.add(studentToFind);
                group.setStudents(students);

                groupRepository.save(group);
                break;
            case "ROLE_TEACHER":
                var teacher = Teacher.builder()
                        .user(currentUser)
                        .build();
                teacherRepository.save(teacher);
                break;
            default:
                break;
        }
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("OK")
                .build();
    }

    public AuthenticationResponse authenticate (AuthenticationRequest request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var userToFind = userService.findUserByEmail(request.getEmail());
        var jwtToken = jwtService.generateToken(userToFind);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("OK")
                .build();
    }

}

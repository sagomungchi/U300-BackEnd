package it.toping.demo.controller;

import it.toping.demo.exception.AppException;
import it.toping.demo.model.Role;
import it.toping.demo.model.RoleName;
import it.toping.demo.model.User;
import it.toping.demo.payload.*;
import it.toping.demo.repository.RoleRepository;
import it.toping.demo.repository.UserRepository;
import it.toping.demo.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(
    @Valid
    @RequestBody
    LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
                loginRequest.getUserNameOrEmail(),
                loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(
    @Valid
    @RequestBody
    SignUpRequest signUpRequest){

        if(userRepository.existsByUserName(signUpRequest.getUserName())) {
            return new ResponseEntity(new ApiResponse(
                    false, "Username is alerady taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByUserEmail(signUpRequest.getUserEmail())){
            return new ResponseEntity(new ApiResponse(
                    false, "Eamil Address is alerady taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        User user = new User(signUpRequest.getTeamName(),
                    signUpRequest.getUserEmail(),
                    signUpRequest.getUserName(),
                    signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setBalance(3000000);

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
        .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
        .fromCurrentContextPath().path("/api/users/{userEmail}")
        .buildAndExpand(result.getUserName()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}

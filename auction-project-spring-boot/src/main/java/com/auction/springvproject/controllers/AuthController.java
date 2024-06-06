package com.auction.springvproject.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.auction.springvproject.models.BankAccount;
import com.auction.springvproject.payload.request.ChangePasswordRequest;
import com.auction.springvproject.repository.BankAccountRepository;
import com.auction.springvproject.security.services.UserDetailsImpl;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auction.springvproject.models.ERole;
import com.auction.springvproject.models.Role;
import com.auction.springvproject.models.User;
import com.auction.springvproject.payload.request.LoginRequest;
import com.auction.springvproject.payload.request.SignupRequest;
import com.auction.springvproject.payload.response.JwtResponse;
import com.auction.springvproject.payload.response.MessageResponse;
import com.auction.springvproject.repository.RoleRepository;
import com.auction.springvproject.repository.UserRepository;
import com.auction.springvproject.security.jwt.JwtUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = new ArrayList<>();

        for (GrantedAuthority role : userDetails.getAuthorities()) {
            roles.add(role.getAuthority());
        }

        logger.info("User with username " + userDetails.getUsername() + " successfully logged in");

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        if (signUpRequest.getUsername().length() > 20 && signUpRequest.getUsername().length() < 4)
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Username should be between size 4 and 20 character"));

        if (signUpRequest.getPassword().length() < 8)
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Password should be at least 8 characters"));

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        //Add initial balance 1000
        BankAccount bankAccount = new BankAccount(user.getUsername(), 1000);
        bankAccountRepository.save(bankAccount);
        logger.info("User successfully registered " + user.getUsername());
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


    @PostMapping("/changepassword")
    public String changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findByUsername(changePasswordRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + changePasswordRequest.getUsername()));
        if (encoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(encoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(user);
        } else
            throw new RuntimeException("The old password is not correct");
        return "Password changed successfully";
    }
}

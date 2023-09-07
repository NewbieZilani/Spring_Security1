package com.spring.securityPractice.controller;

import com.spring.securityPractice.model.UserDto;
import com.spring.securityPractice.service.UserService;
import com.spring.securityPractice.utils.JWTUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping("/hello")
    public String hello(){
        return "Hello";
    }

    @GetMapping("/hello2")
    public String hello2(){
        return "Hello2";
    }


    //Send the Token in body when try to registration
    @PostMapping("/registration")
    public ResponseEntity<Map<String, ?>> register(@RequestBody UserDto userDto, HttpServletResponse response) throws Exception {
        try {
            // Register the user
            UserDto createdUser = userService.createUser(userDto);

            // Generate a JWT token for the registered user
            String token = JWTUtils.generateToken(createdUser.getEmail());

            // Prepare the response
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", token);
            responseBody.put("user", createdUser); // Include user information in the response

            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
        } catch (Exception ex) {
            // Handle registration failure here if needed
            log.error("User registration failed: {}", ex.getMessage());

            // You can return an appropriate response or throw an exception
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Registration failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

}

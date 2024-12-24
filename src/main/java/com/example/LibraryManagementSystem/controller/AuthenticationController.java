package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.exceptions.InvalidCredentialsException;
import com.example.LibraryManagementSystem.exceptions.InvalidRoleException;
import com.example.LibraryManagementSystem.exceptions.NullUserDetails;
import com.example.LibraryManagementSystem.exceptions.UserAlreadyExistException;
import com.example.LibraryManagementSystem.model.AuthenticationResponse;
import com.example.LibraryManagementSystem.model.MessageResponse;
import com.example.LibraryManagementSystem.model.User;
import com.example.LibraryManagementSystem.repository.UserRepository;
import com.example.LibraryManagementSystem.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class AuthenticationController {

    private final AuthenticationService authService;
    private final UserRepository userRepository;

    /**
     * Register a new user.
     * Only accessible by users with ADMIN role.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User request){
        try {
            authService.registerUserByAdmin(request);
            return ResponseEntity.ok(new MessageResponse("User registered successfully."));
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(e.getMessage()));
        } catch (NullUserDetails e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(e.getMessage()));
        } catch (InvalidRoleException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(e.getMessage()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred"));
        }
    }

    /**
     * Authenticate a user (login).
     * Accessible by any authenticated user.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody User request){
        try{
            return ResponseEntity.ok(authService.authenticate(request));
        } catch (NullUserDetails e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthenticationResponse(e.getMessage()));
        } catch (InvalidCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse(e.getMessage()));
        } catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthenticationResponse("User not found"));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthenticationResponse("An unexpected error occurred"));
        }
    }

    /**
     * Delete a user by username.
     * Only accessible by users with ADMIN role.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username){
        try {
            authService.deleteUser(username);
            return ResponseEntity.ok(new MessageResponse(username + " deleted successfully"));
        } catch (NullUserDetails e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(e.getMessage()));
        } catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred"));
        }
    }
}

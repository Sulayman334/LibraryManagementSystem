package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.exceptions.*;
import com.example.LibraryManagementSystem.model.User;
import com.example.LibraryManagementSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try {
            userService.addUser(user);
            return ResponseEntity.ok("User added successfully");
        }catch (NullUserDetails e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.BAD_REQUEST.value(),
                            "Bad Request",
                            e.getMessage(),
                            "/users/add"
                    ));

        }catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            e.getMessage(),
                            "/users/add"
                    ));
        } catch (UserAlreadyExistException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.CONFLICT.value(),
                            "Conflict",
                            e.getMessage(),
                            "/users/add"
                    ));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            e.getMessage(),
                            "/users/add"
                    ));

        }
    }

    @DeleteMapping("delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId){
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (UserNotFound e){
            return ResponseEntity
                   .status(HttpStatus.NOT_FOUND)
                   .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            e.getMessage(),
                            "/users/delete/" + userId
                    ));
        }catch (DatabaseException e){
            return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            e.getMessage(),
                            "/users/delete/" + userId
                    ));
        } catch (Exception e) {
            return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            e.getMessage(),
                            "/users/delete/" + userId
                    ));
        }
    }

    @PutMapping("update/{userId}")
    public ResponseEntity<?> updateUserRole(
            @PathVariable Long userId,
            @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(user, userId);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFound e) {
            return ResponseEntity.status(404).body(e.getMessage()); // User not found
        } catch (NullUserDetails e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Role is null
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Invalid role
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred"); // General server error
        }
    }

    @GetMapping("get/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        List<User> users = userService.getUsers();
        if (users.isEmpty()) {
            throw new UserNotFound("No users found");
        }
        return users;
    }

}

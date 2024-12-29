package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.exceptions.DatabaseException;
import com.example.LibraryManagementSystem.exceptions.NullUserDetails;
import com.example.LibraryManagementSystem.exceptions.UserAlreadyExistException;
import com.example.LibraryManagementSystem.exceptions.UserNotFound;
import com.example.LibraryManagementSystem.model.User;
import com.example.LibraryManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public User addUser(User user) {
        if (user == null) {
            throw new NullUserDetails("User Details cannot be null");
        }

        if (!(Objects.equals(user.getRole(), "ADMIN") || Objects.equals(user.getRole(), "USER"))) {
            throw new IllegalArgumentException("Role must be either ADMIN or USER");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistException("User already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);

    }

    public void deleteUser(Long userId){
        try {
            if (!userRepository.existsById(userId)){
                throw new UserNotFound("User with id " + userId + " does not exist");
            }
            userRepository.deleteById(userId);
        }catch (UserNotFound e){
            throw new UserNotFound("User with id " + userId + " does not exist");
        }catch (DatabaseException e){
            throw new DatabaseException("Error occurred while deleting user with id " + userId);
        }
    }

    public User updateUser(User existing, Long userId) {
        // Validate that the role is not null
        if (existing.getRole() == null) {
            throw new NullUserDetails("User Role cannot be null");
        }

        // Validate that the role is either "ADMIN" or "USER"
        if (!(Objects.equals(existing.getRole(), "ADMIN") || Objects.equals(existing.getRole(), "USER"))) {
            throw new IllegalArgumentException("Role must be either ADMIN or USER");
        }

        // Find the user by ID or throw exception if not found
        User updatedUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFound("User with id " + userId + " does not exist"));

        // Update the role
        updatedUser.setRole(existing.getRole());

        // Save the updated user
        return userRepository.save(updatedUser);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
               .orElseThrow(() -> new UserNotFound("User with id " + userId + " does not exist"));
    }

    public List<User> getUsers() {
        List<User> user = userRepository.findAll();

        if (user.isEmpty()) {
            throw new NullUserDetails("No users found in the database");
        }
        return user;
    }

}

package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.enums.Role;
import com.example.LibraryManagementSystem.exceptions.InvalidCredentialsException;
import com.example.LibraryManagementSystem.exceptions.InvalidRoleException;
import com.example.LibraryManagementSystem.exceptions.NullUserDetails;
import com.example.LibraryManagementSystem.exceptions.UserAlreadyExistException;
import com.example.LibraryManagementSystem.model.AuthenticationResponse;
import com.example.LibraryManagementSystem.model.Token;
import com.example.LibraryManagementSystem.model.User;
import com.example.LibraryManagementSystem.repository.TokenRepository;
import com.example.LibraryManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;


    public AuthenticationResponse register(User request) {
        if (repository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistException(request.getUsername() + " already exists");
        }

        validateField(request.getFirstName(), "User's first name");
        validateField(request.getLastName(), "User's last name");
        validateField(request.getUsername(), "Username");
        validateField(request.getPassword(), "Password");

        if (request.getRole() == null) {
            throw new NullUserDetails("User's role cannot be null");
        }

        // Validate role if necessary
        if (request.getRole() != Role.ROLE_ADMIN && request.getRole() != Role.ROLE_LIBRARIAN){
            throw new InvalidRoleException("Role must be a valid role  " + request.getRole().name());
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setInitial(request.getInitial());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        user = repository.save(user);

        String jwt = jwtService.generateToken(user);

        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt);
    }

    private void saveUserToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    private void validateField(String field, String fieldName) {
        if (field == null || field.trim().isEmpty()) {
            throw new NullUserDetails(fieldName + " cannot be null or empty");
        }
    }


    public AuthenticationResponse authenticate(User request){
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new NullUserDetails("Username cannot be null or empty");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new NullUserDetails("Password cannot be null or empty");
        }

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(), request.getPassword()
            ));

        }catch (Exception e){
            throw new InvalidCredentialsException("Invalid username or password");
        }


        User user = repository.findByUsername(request.getUsername())
                .orElseThrow(()->new UsernameNotFoundException("Username " + request.getUsername()));
        String token = jwtService.generateToken(user);

        revokeAllTokenByUser(user);


        saveUserToken(token,user);

        return new AuthenticationResponse(token);

    }


    public void deleteUser(String username){
        if (username == null){
            throw new NullUserDetails("Username cannot be null");
        }

        User user = repository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("Username " + username +" not found"));
        tokenRepository.deleteByUser(user);
        repository.delete(user);
    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokensListedByUser = tokenRepository.findAllAccessTokensByUser(Math.toIntExact(user.getId()));
        if (!validTokensListedByUser.isEmpty()){
            validTokensListedByUser.forEach(t -> t.setLoggedOut(true));
        }
        tokenRepository.saveAll(validTokensListedByUser);
    }


}

package com.example.LibraryManagementSystem.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "librarians")
@Data
public class Librarian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String first_name;

    @Column(name = "last_name",nullable = false)
    private String last_name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password", unique = true)
    private String password;

    @Column(name = "username", unique = true)
    private String username;
    
}

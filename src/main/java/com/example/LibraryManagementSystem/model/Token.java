package com.example.LibraryManagementSystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "is_logged_out")
    private Boolean loggedOut;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}

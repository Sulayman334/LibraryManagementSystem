package com.example.LibraryManagementSystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "borrow_books")
public class BorrowBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column( name = "borrowed_date",nullable = false)
    private LocalDate borrow_date;

    @Column(name = "return_date", nullable = false)
    private LocalDate return_date;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "returned", nullable = false)
    private Boolean returned = false;
}

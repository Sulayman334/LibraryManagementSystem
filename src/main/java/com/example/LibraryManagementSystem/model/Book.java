package com.example.LibraryManagementSystem.model;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @Column(name = "availability", nullable = false)
    private Boolean available;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "available_copies")
    private Integer availableCopies;

    @Column(name = "total_copies")
    private Integer totalCopies;


    public void borrowBook(){
        if(availableCopies > 0){
            availableCopies--;
        }else{
            throw new IllegalStateException("No available copies to borrow");
        }
    }

    public void returnBook(){
        availableCopies++;
        available = true;
    }

}

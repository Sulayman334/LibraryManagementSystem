package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    boolean existsByTitleAndAuthor(String title, String author);

    boolean existsByIsbn(String isbn);
}

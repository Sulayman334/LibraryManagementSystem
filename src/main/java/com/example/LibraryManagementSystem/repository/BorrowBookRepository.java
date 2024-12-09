package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.model.BorrowBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowBookRepository extends JpaRepository<BorrowBook,Long> {

}

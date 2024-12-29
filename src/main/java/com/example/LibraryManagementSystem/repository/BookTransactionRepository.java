package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.functionality.BookTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookTransactionRepository extends JpaRepository<BookTransaction,Long> {

}

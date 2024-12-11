package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.model.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibrarianRepository extends JpaRepository<Librarian,Long> {

    Optional<Object> findByUsername(String username);
}

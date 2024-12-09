package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.model.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibrarianRepository extends JpaRepository<Librarian,Long> {

}

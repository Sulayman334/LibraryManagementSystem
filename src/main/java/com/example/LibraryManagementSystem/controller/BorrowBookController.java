package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.service.BorrowBookService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/borrowBook")
public class BorrowBookController {

    private final BorrowBookService borrowBookService;

    @PostMapping("/borrow/{studentId}/{bookId}")
    public ResponseEntity<String> borrowBook(@PathVariable Long studentId, @PathVariable Long bookId) {
        try {
            borrowBookService.borrowBook(studentId, bookId);
            return ResponseEntity.ok("Book borrowed successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }


    @PostMapping("/return/{borrowBookId}")
    public ResponseEntity<String> returnBook(@PathVariable Long borrowBookId){
        borrowBookService.returnBook(borrowBookId);
        return ResponseEntity.ok("Book returned successfully");
    }

}

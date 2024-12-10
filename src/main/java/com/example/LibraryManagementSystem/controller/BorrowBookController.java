package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.service.BorrowBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/borrowBook")
public class BorrowBookController {

    private final BorrowBookService borrowBookService;

    @PostMapping("/borrow/{studentId}/{bookId}")
    public ResponseEntity<String> borrowBook(@PathVariable Long studentId, @PathVariable Long bookId){
        borrowBookService.borrowBook(studentId, bookId);
        return ResponseEntity.ok("Book borrowed successfully");
    }

    @PostMapping("/return/{borrowBookId}")
    public ResponseEntity<String> returnBook(Long borrowBookId){
        borrowBookService.returnBook(borrowBookId);
        return ResponseEntity.ok("Book returned successfully");
    }

}

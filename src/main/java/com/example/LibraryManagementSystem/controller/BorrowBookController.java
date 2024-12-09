package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.service.BorrowBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/borrowBook")
public class BorrowBookController {

    private final BorrowBookService borrowBookService;

    @PostMapping("/borrowBook")
    public ResponseEntity<String> borrowBook(@RequestParam Long studentId,@RequestParam Long bookId){
        borrowBookService.borrowBook(studentId, bookId);
        return ResponseEntity.ok("Book borrowed successfully");
    }

    @PostMapping("/returnBook")
    public ResponseEntity<String> returnBook(Long borrowBookId){
        borrowBookService.returnBook(borrowBookId);
        return ResponseEntity.ok("Book returned successfully");
    }

}

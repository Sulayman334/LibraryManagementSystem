package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.exceptions.*;
import com.example.LibraryManagementSystem.service.BookTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book-transaction")
public class BookTransactionController {

    private final BookTransactionService bookTransactionService;


    @PostMapping("/borrow/{studentId}/{bookId}")
    public ResponseEntity<?> borrowBook(@PathVariable Long studentId, @PathVariable Long bookId) {
        try {
            bookTransactionService.borrowBook(studentId, bookId);
            return ResponseEntity.ok("Book borrowed successfully");
        } catch (StudentNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            e.getMessage(),
                            "/api/book-transaction/borrow/" + studentId + "/" + bookId
                    ));
        }catch (BookNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            e.getMessage(),
                            "/api/book-transaction/borrow/" + studentId + "/" + bookId
                    ));
        }catch (NullUserDetails e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.BAD_REQUEST.value(),
                            "Bad Request",
                            e.getMessage(),
                            "/api/book-transaction/borrow/" + studentId + "/" + bookId
                    ));
        }catch (NullBookDetails e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.BAD_REQUEST.value(),
                            "Bad Request",
                            e.getMessage(),
                            "/api/book-transaction/borrow/" + studentId + "/" + bookId
                    ));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            e.getMessage(),
                            "/api/book-transaction/borrow/" + studentId + "/" + bookId
                    ));
        }
    }


    @PostMapping("/return/{borrowBookId}")
    public ResponseEntity<?> returnBook(@PathVariable Long borrowBookId) {
        try {
            bookTransactionService.returnBook(borrowBookId);
            return ResponseEntity.ok("Book returned successfully.");
        } catch (BookNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                   .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            e.getMessage(),
                            "/api/book-transaction/return/" + borrowBookId
                    ));
        }catch (BookAlreadyExistsException e) {
            return ResponseEntity
                   .status(HttpStatus.CONFLICT)
                   .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.CONFLICT.value(),
                            "Conflict",
                            e.getMessage(),
                            "/api/book-transaction/return/" + borrowBookId
                    ));
        }catch (Exception e){
            return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            e.getMessage(),
                            "/api/book-transaction/return/" + borrowBookId
                    ));
        }
    }
}

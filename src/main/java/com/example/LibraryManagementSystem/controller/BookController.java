package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.exceptions.*;
import com.example.LibraryManagementSystem.model.Book;
import com.example.LibraryManagementSystem.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")

public class BookController {

    private final BookService bookService;



    @PostMapping("/add")
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        try {
            Book createdBook = bookService.addBook(book);
            return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
        } catch (BookAlreadyExistsException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.CONFLICT.value(),
                            "Conflict",
                            e.getMessage(),
                            "/api/books/add"
                    ));
        }catch (NullBookDetails e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.BAD_REQUEST.value(),
                            "Bad Request",
                            e.getMessage(),
                            "/api/books/add"
                    ));
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            e.getMessage(),
                            "/api/books/add"
                    ));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id){
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok("Book has been successfully deleted");
        } catch (BookNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            e.getMessage(),
                            "/api/books/delete/" + id
                    ));

        } catch (DatabaseException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            e.getMessage(),
                            "/api/books/delete/" + id
                    ));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            e.getMessage(),
                            "/api/books/delete/" + id
                    ));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBook(@RequestBody Book updatedBook, @PathVariable Long id) {
        try {
            Book result = bookService.updateBook(updatedBook, id);
            return ResponseEntity.ok(result);
        } catch (BookNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            e.getMessage(),
                            "/api/books/update/" + id
                    ));
        } catch (BookAlreadyExistsException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.CONFLICT.value(),
                            "Conflict",
                            e.getMessage(),
                            "/api/books/update/" + id
                    ));
        } catch (NullBookDetails e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.BAD_REQUEST.value(),
                            "Bad Request",
                            e.getMessage(),
                            "/api/books/update/" + id
                    ));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            e.getMessage(),
                            "/api/books/update/" + id
                    ));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?>getStudentsById(@PathVariable Long id){
        try {
            Book book = bookService.getBookById(id);
            return ResponseEntity.ok(book);
        } catch (BookNotFoundException e) {
            return ResponseEntity
                   .status(HttpStatus.NOT_FOUND)
                   .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            e.getMessage(),
                            "/api/books/get/" + id
                    ));
        } catch (Exception e) {
            return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            e.getMessage(),
                            "/api/books/get/" + id
                    ));
        }
    }

    @RequestMapping("/all")
    public List<Book> getAllBooks() {
       List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            throw new BookNotFoundException("No books found in the database");
        }
        return books;
    }

}

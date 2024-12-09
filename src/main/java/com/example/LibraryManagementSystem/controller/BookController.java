package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.model.Book;
import com.example.LibraryManagementSystem.model.Librarian;
import com.example.LibraryManagementSystem.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")

public class BookController {

    private final BookService bookService;

    @RequestMapping("/all")
    public List<Book> getAllBooks() {
        return bookService.getBooks();
    }

    @PostMapping("/add")
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @GetMapping("/find/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PutMapping("/update/{id}")
    public Book updateBook(@RequestBody Book updatedBook, @PathVariable Long id) {
        updatedBook.setId(id);
        return bookService.addBook(updatedBook);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }


}

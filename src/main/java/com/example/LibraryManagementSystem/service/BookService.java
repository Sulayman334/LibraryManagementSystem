package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.model.Book;
import com.example.LibraryManagementSystem.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Book book, Long id) {
        Book existingBook = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPublicationYear(book.getPublicationYear());
        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long BookId){
        if (bookRepository.findById(BookId).isPresent()) {
            bookRepository.deleteById(BookId);
        }else {
            throw new RuntimeException("Book not found");
        }
    }

    public Book getBookById(Long id) {
        if (bookRepository.findById(id).isPresent()) {
            return bookRepository.findById(id).orElse(null);
        }
        return null;
    }

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

}

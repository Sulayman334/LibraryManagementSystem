package com.example.LibraryManagementSystem.service;
import com.example.LibraryManagementSystem.exceptions.BookAlreadyExistsException;
import com.example.LibraryManagementSystem.exceptions.BookNotFoundException;
import com.example.LibraryManagementSystem.exceptions.NullBookDetails;
import com.example.LibraryManagementSystem.model.Book;
import com.example.LibraryManagementSystem.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;


@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;

    public Book addBook(Book book) {
        // Validate that the book object is not null
        if (book == null) {
            throw new NullBookDetails("Book details cannot be null");
        }
        // Validate that the title and author combination is unique
        if (bookRepository.existsByTitleAndAuthor(book.getTitle(), book.getAuthor())) {
            throw new BookAlreadyExistsException(
                    "A book with title '" + book.getTitle() + "' by author '" + book.getAuthor() + "' already exists"
            );
        }
        // Validate that the ISBN is unique
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new BookAlreadyExistsException("A book with ISBN '" + book.getIsbn() + "' already exists");
        }
        // Save and return the new book
        return bookRepository.save(book);
    }


    public void deleteBook(Long id) {
        try {
            if (!bookRepository.existsById(id)) {
                throw new BookNotFoundException("Book with id '" + id + "' not found");
            }
            bookRepository.deleteById(id);
        } catch (BookNotFoundException e) {
            throw new BookNotFoundException("Book with id '" + id + "' not found");
        } catch (DataAccessException e) {
            throw new RuntimeException("An error occurred while deleting the book" + id);
        }
    }


    public Book updateBook(Book updatedBook, Long id) {
        if (updatedBook == null) {
            throw new NullBookDetails("Book details cannot be null");
        }

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id '" + id + "' not found"));

        validateField(updatedBook.getTitle(), "Book title");
        existingBook.setTitle(updatedBook.getTitle());

        validateField(updatedBook.getAuthor(), "Book author");
        existingBook.setAuthor(updatedBook.getAuthor());

        if (updatedBook.getPublicationYear() == null) {
            throw new NullBookDetails("Book publication year cannot be null");
        }
        existingBook.setPublicationYear(updatedBook.getPublicationYear());

        validateIsbn(updatedBook.getIsbn(), existingBook);
        existingBook.setIsbn(updatedBook.getIsbn());

        if (updatedBook.getTotalCopies() == null) {
            throw new NullBookDetails("Book total copies cannot be null");
        }
        existingBook.setTotalCopies(updatedBook.getTotalCopies());

        if (updatedBook.getAvailableCopies() == null) {
            throw new NullBookDetails("Book available copies cannot be null");
        }
        existingBook.setAvailableCopies(updatedBook.getAvailableCopies());

        if (updatedBook.getAvailable() == null) {
            throw new NullBookDetails("Book availability status cannot be null");
        }
        existingBook.setAvailable(updatedBook.getAvailable());

        return bookRepository.save(existingBook);
    }

    private void validateField(String field, String fieldName) {
        if (field == null || field.trim().isEmpty()) {
            throw new NullBookDetails(fieldName + " cannot be null or empty");
        }
    }

    private void validateIsbn(String newIsbn, Book existingBook) {
        if (newIsbn == null || newIsbn.trim().isEmpty()) {
            throw new NullBookDetails("Book ISBN cannot be null");
        }
        if (!newIsbn.equals(existingBook.getIsbn()) && bookRepository.existsByIsbn(newIsbn)) {
            throw new BookAlreadyExistsException("A book with ISBN '" + newIsbn + "' already exists");
        }
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                    .orElseThrow(() -> new BookNotFoundException("Book with id '" + id + "' Not found"));

    }

    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
        throw new NullBookDetails("No books found in the database");
        }
        return books;
    }

}


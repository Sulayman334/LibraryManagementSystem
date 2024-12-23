package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.exceptions.*;
import com.example.LibraryManagementSystem.functionality.BookTransaction;
import com.example.LibraryManagementSystem.model.Book;
import com.example.LibraryManagementSystem.model.Student;
import com.example.LibraryManagementSystem.repository.BookRepository;
import com.example.LibraryManagementSystem.repository.BookTransactionRepository;
import com.example.LibraryManagementSystem.repository.StudentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BookTransactionService {

    @Autowired
    private final BookTransactionRepository borrowBookRepository;

    @Autowired
    private final StudentRepository studentRepository;

    @Autowired
    private final BookRepository bookRepository;


    @Transactional
    public void borrowBook(Long studentId, Long bookId) {
        // Retrieve Student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student with ID " + studentId + " not found"));

        // Retrieve Book
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found"));

        // Check if copies are available
        if (book.getAvailableCopies() <= 0) {
            throw new BookNotFoundException("No available copies of '" + book.getTitle() + "' to borrow");
        }

        // Create and populate the borrowing transaction
        BookTransaction borrowBook = new BookTransaction();
        borrowBook.setStudent(student);
        borrowBook.setBook(book);
        borrowBook.setBorrow_date(LocalDate.now());
        borrowBook.setDueDate(LocalDate.now().plusWeeks(1)); // Configurable in future
        borrowBook.setReturned(false);

        // Validate borrowBook object
        validateBorrowTransaction(borrowBook);

        // Save the transaction
        borrowBookRepository.save(borrowBook);

        // Update and save book inventory
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        // Log success (optional)
        System.out.println("Book '" + book.getTitle() + "' borrowed successfully by " +
                student.getFirstName()+student.getLastName());
    }

    // Helper method to validate borrow transaction details
    private void validateBorrowTransaction(BookTransaction transaction) {
        if (transaction.getStudent() == null) {
            throw new NullUserDetails("Student information is missing in the transaction.");
        }
        if (transaction.getBook() == null) {
            throw new NullBookDetails("Book information is missing in the transaction.");
        }
        if (transaction.getBorrow_date() == null) {
            throw new IllegalArgumentException("Borrow date is missing in the transaction.");
        }
        /*if (transaction.getDueDate() == null) {
            throw new IllegalArgumentException("Due date is missing in the transaction.");
        }
        if (transaction.getReturned() == null) {
            throw new IllegalArgumentException("Returned status is missing in the transaction.");
        }*/
    }


    public void returnBook(Long borrowBookId) {

        // Fetch the BorrowBook record, throw exception if not found
        BookTransaction returnBook = borrowBookRepository.findById(borrowBookId)
                .orElseThrow(() -> new BookNotFoundException("Book transaction not found for ID: " + borrowBookId));

        // Check if the book has already been returned
        if (returnBook.getReturned()) {
            throw new BookAlreadyExistsException("Book has already been returned. Transaction ID: " + borrowBookId);
        }

        // Update the return date and status of the book transaction
        Book book = returnBook.getBook();
        returnBook.setReturn_date(LocalDate.now());
        returnBook.setReturned(true);
        borrowBookRepository.save(returnBook);

        // Update the book's available copies count
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        // Optional: log the successful return (if a logging mechanism is available)
        // logger.info("Book returned successfully. Transaction ID: " + borrowBookId + ", Book ID: " + book.getId());
    }

}

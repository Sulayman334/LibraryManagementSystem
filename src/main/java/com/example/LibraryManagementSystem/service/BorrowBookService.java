package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.model.Book;
import com.example.LibraryManagementSystem.model.BorrowBook;
import com.example.LibraryManagementSystem.model.Student;
import com.example.LibraryManagementSystem.repository.BookRepository;
import com.example.LibraryManagementSystem.repository.BorrowBookRepository;
import com.example.LibraryManagementSystem.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BorrowBookService {

    private final BorrowBookRepository borrowBookRepository;

    private final StudentRepository studentRepository;

    private final BookRepository bookRepository;

    public void borrowBook(Long studentId, Long bookId){
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        if ((book.getAvailableCopies() <= 0)){
            throw new RuntimeException("No available copies to borrow");
        }

        BorrowBook borrowBook = new BorrowBook();
        borrowBook.setStudent(student);
        borrowBook.setBook(book);
        borrowBook.setBorrow_date(java.time.LocalDate.now());
        borrowBook.setReturn_date(LocalDate.now().plusWeeks(1));
        borrowBook.setReturned(false);
        borrowBookRepository.save(borrowBook);

    }

    public void returnBook(Long borrowBookId){

       BorrowBook borrowBook = borrowBookRepository.findById(borrowBookId)
               .orElseThrow(()-> new EntityNotFoundException("Borrow Book not found"));

       if ((borrowBook.getReturned())){
           throw new RuntimeException("Book already returned");
       }

       Book book = borrowBook.getBook();
       book.returnBook();
       bookRepository.save(book);

       borrowBook.setReturned(true);
       borrowBook.setReturn_date(LocalDate.now());
       borrowBookRepository.save(borrowBook);
    }



}

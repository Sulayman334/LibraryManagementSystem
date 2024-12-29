package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.exceptions.*;
import com.example.LibraryManagementSystem.model.Student;
import com.example.LibraryManagementSystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;


    @PostMapping("/add")
    public ResponseEntity<?> addStudent(@RequestBody Student student) {
        try {
            // Attempt to add the student
            Student addedStudent = studentService.addStudent(student);
            return new ResponseEntity<>(addedStudent, HttpStatus.CREATED);

        } catch (NullUserDetails e) {
            // Handle NullUserDetails exception for missing/invalid input
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.BAD_REQUEST.value(),
                            "Bad Request",
                            e.getMessage(),
                            "/api/students/add"
                    ));

        } catch (StudentAlreadyExistsException e) {
            // Handle StudentAlreadyExistsException for duplicate email
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.CONFLICT.value(),
                            "Conflict",
                            e.getMessage(),
                            "/api/students/add"
                    ));

        } catch (Exception e) {
            // Handle all other general exceptions
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            e.getMessage(),
                            "/api/students/add"
                    ));
        }
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.ok("Student has been Successfully deleted");
        } catch (StudentNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            "Student not found with the given ID: " + id,
                            "/api/students/delete/" + id
                    ));

        } catch (DatabaseException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            e.getMessage(),
                            "/api/students/delete/" + id
                    ));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            e.getMessage(),
                            "/api/students/delete/" + id
                    ));
        }

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStudent(@RequestBody Student student, @PathVariable Long id) {
        try {
            // Call service method to update the student
            Student updatedStudent = studentService.updateStudent(student, id);
            return ResponseEntity.ok(updatedStudent); // Return 200 OK with updated details
        } catch (StudentNotFoundException e) {
            // Handle case when the student with the provided ID is not found
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            e.getMessage(),
                            "/api/students/update/" + id
                    ));
        } catch (NullUserDetails e) {
            // Handle case when required details are missing
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.BAD_REQUEST.value(),
                            "Bad Request",
                            e.getMessage(),
                            "/api/students/update/" + id
                    ));
        } catch (Exception e) {
            // Handle general or unexpected server errors
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            "An unexpected error occurred. Please try again later.",
                            "/api/students/update/" + id
                    ));
        }
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        try {
            Student student = studentService.getStudentById(id);
            return ResponseEntity.ok(student); // Return 200 OK with the student details
        } catch (StudentNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            e.getMessage(),
                            "/api/students/" + id
                    ));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            "An unexpected error occurred. Please try again later.",
                            "/api/students/" + id
                    ));
        }
    }


    @GetMapping("/all")
    public List<Student> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            throw new StudentNotFoundException("No students found in the database");
        }
        return students;
    }

}


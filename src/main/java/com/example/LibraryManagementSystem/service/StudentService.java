package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.exceptions.DatabaseException;
import com.example.LibraryManagementSystem.exceptions.NullUserDetails;
import com.example.LibraryManagementSystem.exceptions.StudentAlreadyExistsException;
import com.example.LibraryManagementSystem.exceptions.StudentNotFoundException;
import com.example.LibraryManagementSystem.model.Student;
import com.example.LibraryManagementSystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public Student addStudent(Student student) {
        // Check if first name is empty or null
        if (student.getFirstName() == null || student.getFirstName().trim().isEmpty()) {
            throw new NullUserDetails("Student first name cannot be empty");
        }

        // Check if last name is empty or null
        if (student.getLastName() == null || student.getLastName().trim().isEmpty()) {
            throw new NullUserDetails("Student last name cannot be empty");
        }

        //change the student.getname to student

        // Check if email is empty or null
        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            throw new NullUserDetails("Student email cannot be empty");
        }

        // Check if the student already exists by email
        if (studentRepository.existsByEmail(student.getEmail().trim())) {
            throw new StudentAlreadyExistsException("Student with email " + student.getEmail() + " already exists");
        }

        // Save the student record
        return studentRepository.save(student);
    }




    public void deleteStudent(Long id) {
        try {
            if (!studentRepository.existsById(id)){
                throw new StudentNotFoundException("Student with id " + id + "is not found");
            }
            studentRepository.deleteById(id);

        } catch (StudentNotFoundException e) {
            throw new StudentNotFoundException("Student with id " + id + " is not found");
        } catch (DatabaseException e) {
            throw new DatabaseException("Error occurred while deleting student with id " + id);
        }
    }




    public Student updateStudent(Student updatedStudent, Long id) {
        if (updatedStudent == null) {
            throw new NullUserDetails("Student details cannot be null");
        }

        // Retrieve the existing student or throw an exception if not found
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));

        // Validate and update fields
        if (updatedStudent.getFirstName() != null && !updatedStudent.getFirstName().trim().isEmpty()) {
            existingStudent.setFirstName(updatedStudent.getFirstName());
        } else {
            throw new NullUserDetails("Student first name cannot be null or empty");
        }

        if (updatedStudent.getLastName() != null && !updatedStudent.getLastName().trim().isEmpty()) {
            existingStudent.setLastName(updatedStudent.getLastName());
        } else {
            throw new NullUserDetails("Student last name cannot be null or empty");
        }

        if (updatedStudent.getEmail() != null && !updatedStudent.getEmail().trim().isEmpty()) {
            // Check for email duplication
            boolean emailExists = studentRepository.existsByEmail(updatedStudent.getEmail());
            if (emailExists && !updatedStudent.getEmail().equals(existingStudent.getEmail())) {
                throw new StudentAlreadyExistsException("Student with email " + updatedStudent.getEmail() + " already exists");
            }
            existingStudent.setEmail(updatedStudent.getEmail());
        } else {
            throw new NullUserDetails("Student email cannot be null or empty");
        }

        // Update optional fields
        if (updatedStudent.getMiddleName() != null) {
            existingStudent.setMiddleName(updatedStudent.getMiddleName());
        }

        // Save the updated student and return it
        return studentRepository.save(existingStudent);
    }



    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("No student found with id: " + id));
    }

    public List<Student> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            throw new NullUserDetails("No students found in the database");
        }
        return students;

    }
}

package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.model.Student;
import com.example.LibraryManagementSystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public Student updateStudent(Student updatedStudent,Long id) {
        Student existingStudent = studentRepository.findById(id).orElse(null);
        if (existingStudent != null) {
            existingStudent.setFirst_name(updatedStudent.getFirst_name());
            existingStudent.setLast_name(updatedStudent.getLast_name());
            existingStudent.setEmail(updatedStudent.getEmail());
            return studentRepository.save(existingStudent);
        }
        return null;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

}

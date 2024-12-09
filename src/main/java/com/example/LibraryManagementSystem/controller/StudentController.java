package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.model.Student;
import com.example.LibraryManagementSystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/addStudent")
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @DeleteMapping("/deleteStudent/{id}")
    public void deleteStudentById(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @PutMapping("/update/{id}")
    public Student updateStudentById(@RequestBody Student student, @PathVariable Long id){
        return studentService.updateStudent(student, id);
    }

    @GetMapping("/get/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @GetMapping("/all")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }


}

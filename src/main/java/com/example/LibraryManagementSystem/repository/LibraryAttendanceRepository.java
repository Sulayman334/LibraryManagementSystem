package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.enums.AttendanceStatus;
import com.example.LibraryManagementSystem.functionality.LibraryAttendance;
import com.example.LibraryManagementSystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibraryAttendanceRepository extends JpaRepository<LibraryAttendance,Long> {

    List<LibraryAttendance> findByStatus(AttendanceStatus status);

    List<LibraryAttendance> findByStudentOrderByEntryTimeDesc(Student student);

    Optional<LibraryAttendance> findByStudentAndStatus(Student student, AttendanceStatus status);
}

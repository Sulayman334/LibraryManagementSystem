package com.example.LibraryManagementSystem.functionality;

import com.example.LibraryManagementSystem.enums.AttendanceStatus;
import com.example.LibraryManagementSystem.model.Student;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "library_attendance")
@Data
public class LibraryAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;


    @Column(name = "entry_time", nullable = false)
    private LocalDateTime entryTime;

    @Column(name = "exit_time" , nullable = true)
    private LocalDateTime exitTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status", nullable = false)
    private AttendanceStatus status;

}

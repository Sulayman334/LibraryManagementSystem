package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.enums.AttendanceStatus;
import com.example.LibraryManagementSystem.exceptions.NullUserDetails;
import com.example.LibraryManagementSystem.exceptions.StudentAlreadyExistsException;
import com.example.LibraryManagementSystem.exceptions.StudentNotFoundException;
import com.example.LibraryManagementSystem.functionality.LibraryAttendance;
import com.example.LibraryManagementSystem.model.Student;
import com.example.LibraryManagementSystem.repository.LibraryAttendanceRepository;
import com.example.LibraryManagementSystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibraryAttendanceService {

    private final LibraryAttendanceRepository libraryAttendanceRepository;

    private final StudentRepository studentRepository;



    //@Transactional
    public LibraryAttendance registerEntry(Long studentId) {
        // Fetch the student from the repository
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + studentId));

        // Ensure that the student object is not null before proceeding
        if (student == null) {
            throw new NullUserDetails("Student cannot be null.");
        }

        // Check if the student already has an active entry
        Optional<LibraryAttendance> existingEntry = libraryAttendanceRepository
                .findByStudentAndStatus(student, AttendanceStatus.ENTERED);

        if (existingEntry.isPresent()) {
            throw new StudentAlreadyExistsException("Student is already in the library: " + studentId);
        }

        // Ensure that entryTime is not null and set it
        LocalDateTime entryTime = LocalDateTime.now();
        if (entryTime == null) {
            throw new NullUserDetails("Entry time cannot be null.");
        }

        // Create new library attendance entry
        LibraryAttendance entry = new LibraryAttendance();
        entry.setStudent(student);  // Student is set here, it should not be null
        entry.setEntryTime(entryTime); // Entry time is set here, it should not be null
        entry.setExitTime(null); // Set exit time to null, as the student has not exited yet
        entry.setStatus(AttendanceStatus.ENTERED); // Mark the status as 'ENTERED', ensuring it's not null

        // Save the entry and return it
        return libraryAttendanceRepository.save(entry);
    }


    public LibraryAttendance registerExit(Long studentId) {
        // Fetch the student from the repository
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student could not be found with ID: " + studentId));

        // Ensure the student object is not null
        if (student == null) {
            throw new NullUserDetails("Student cannot be null.");
        }

        // Fetch the student's active library entry
        LibraryAttendance exit = libraryAttendanceRepository
                .findByStudentAndStatus(student, AttendanceStatus.ENTERED)
                .orElseThrow(() -> new StudentNotFoundException("Student is not currently in the library."));

        // Set exit time and ensure it's not null
        LocalDateTime exitTime = LocalDateTime.now();
        if (exitTime == null) {
            throw new NullUserDetails("Exit time cannot be null.");
        }

        // Set the status to EXITED and ensure it's not null
        exit.setExitTime(exitTime); // Set the exit time
        exit.setStatus(AttendanceStatus.EXITED); // Mark the status as EXITED
        if (exit.getStatus() == null) {
            throw new NullUserDetails("Status cannot be null.");
        }

        // Save the updated attendance record
       return libraryAttendanceRepository.save(exit);
        //return attendance;
    }


    public List<LibraryAttendance> getCurrentLibraryOccupants() {
        // Fetch all students currently in the library (status: ENTERED)
        List<LibraryAttendance> occupants = libraryAttendanceRepository.findByStatus(AttendanceStatus.ENTERED);

        if (occupants.isEmpty()) {
            throw new StudentNotFoundException("No students are currently in the library.");
        }

        return occupants; // Returns the list of occupants if not empty
    }




    public List<LibraryAttendance> getStudentAttendanceHistory(Long studentId){
        Student student = studentRepository.findById(studentId).
                orElseThrow(() -> new StudentNotFoundException("Student Could not found"));

        return libraryAttendanceRepository.findByStudentOrderByEntryTimeDesc(student);
    }

}



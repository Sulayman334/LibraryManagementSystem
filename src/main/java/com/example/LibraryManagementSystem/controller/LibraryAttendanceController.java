package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.exceptions.GlobalExceptionHandler;
import com.example.LibraryManagementSystem.exceptions.NullUserDetails;
import com.example.LibraryManagementSystem.exceptions.StudentAlreadyExistsException;
import com.example.LibraryManagementSystem.exceptions.StudentNotFoundException;
import com.example.LibraryManagementSystem.functionality.LibraryAttendance;
import com.example.LibraryManagementSystem.service.LibraryAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/library-attendance")
public class LibraryAttendanceController {

    private final LibraryAttendanceService libraryAttendanceService;


    @PostMapping("/enter/{id}")
    public ResponseEntity<?> entry(@PathVariable Long id) {
        try {
            LibraryAttendance entry = libraryAttendanceService.registerEntry(id);
            return ResponseEntity.ok("Student with id " + id +
                    " has successfully entered the library at " + entry.getEntryTime());
        } catch (StudentNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            e.getMessage(),
                            "/api/library-entries/enter/" + id
                    ));
        } catch (NullUserDetails e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.BAD_REQUEST.value(),
                            "Bad Request",
                            e.getMessage(),
                            "/api/library-entries/enter/" + id
                    ));
        } catch (StudentAlreadyExistsException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.CONFLICT.value(),
                            "Conflict",
                            e.getMessage(),
                            "/api/library-entries/enter/" + id
                    ));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            e.getMessage(),
                            "/api/library-entries/enter/" + id
                    ));
        }
    }


    @PostMapping("/exit/{id}")
    public ResponseEntity<?> exit(@PathVariable Long id) {
        try {
            LibraryAttendance exit = libraryAttendanceService.registerExit(id);
            return ResponseEntity.ok("Student with ID " + id +
                    " has exited the library successfully at " + exit.getExitTime());
        } catch (StudentNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            e.getMessage(),
                            "/api/library-entries/exit/" + id
                    ));
        } catch (NullUserDetails e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.BAD_REQUEST.value(),
                            "Bad Request",
                            e.getMessage(),
                            "/api/library-entries/exit/" + id
                    ));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            e.getMessage(),
                            "/api/library-entries/exit/" + id
                    ));
        }
    }


    @GetMapping("/occupants")
    public ResponseEntity<?> getCurrentLibraryOccupants() {
        try {
            List<LibraryAttendance> occupants = libraryAttendanceService.getCurrentLibraryOccupants();
            return ResponseEntity.ok(occupants);
        } catch (StudentNotFoundException e){
            return ResponseEntity
                   .status(HttpStatus.NOT_FOUND)
                   .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            e.getMessage(),
                            "/api/library-entries/occupants"
                    ));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }


    @GetMapping("/attendance/history/{studentId}")
    public ResponseEntity<?> getStudentAttendanceHistory(@PathVariable Long studentId) {
        try {
            List<LibraryAttendance> attendanceHistory = libraryAttendanceService.getStudentAttendanceHistory(studentId);
            return ResponseEntity.ok(attendanceHistory);
        } catch (StudentNotFoundException e) {
            return ResponseEntity
                   .status(HttpStatus.NOT_FOUND)
                   .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            e.getMessage(),
                            "/api/library-entries/attendance/history/" + studentId
                    ));
        }catch (Exception e) {
            return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(new GlobalExceptionHandler.ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            e.getMessage(),
                            "/api/library-entries/attendance/history/" + studentId
                    ));
        }
    }
}


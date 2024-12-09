package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.model.Librarian;
import com.example.LibraryManagementSystem.service.LibrarianService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/librarians")
public class LibrarianController {

    private final LibrarianService librarianService;

    @GetMapping("/all")
    public List<Librarian> getAllLibrarians(){
        return librarianService.getLibrarians();
    }

    @PostMapping("/addLibrarian")
    public Librarian addLibrarian(@RequestBody Librarian librarian){
        return librarianService.addLibrarian(librarian);
    }

    @DeleteMapping("/deleteLibrarianById/{id}")
    public void deleteLibrarianById(@PathVariable Long id){
        librarianService.deleteLibrarian(id);
    }


    @PutMapping("/updateLibrarianById/{id}")
    public Librarian updateLibrarian(@RequestBody Librarian updatedLibrarian,@PathVariable Long id){
        return librarianService.updateLibrarian(updatedLibrarian,id);
    }


    @GetMapping("/getLibrarianById/{id}")
    public Librarian getLibrarianById(@PathVariable Long id){
        return librarianService.getLibrarianById(id);
    }
}

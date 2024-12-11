package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.model.Librarian;
import com.example.LibraryManagementSystem.repository.LibrarianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LibrarianService implements UserDetailsService {

    private final LibrarianRepository librarianRepository;

    public Librarian addLibrarian(Librarian librarian) {
        return librarianRepository.save(librarian);
    }

    public void deleteLibrarian(Long LibrarianId) {
        librarianRepository.deleteById(LibrarianId);
    }

    public Librarian updateLibrarian(Librarian librarian, Long id) {
        if (librarianRepository.findById(id).isPresent()) {
            Librarian existingLibrarian = librarianRepository.findById(id).get();
            existingLibrarian.setFirst_name(librarian.getFirst_name());
            existingLibrarian.setLast_name(librarian.getLast_name());
            existingLibrarian.setEmail(librarian.getEmail());
            existingLibrarian.setPassword(librarian.getPassword());
            existingLibrarian.setUsername(librarian.getUsername());
            return librarianRepository.save(existingLibrarian);
        } else {
            throw new RuntimeException("Librarian not found with id: " + id);
        }
    }

    public Librarian getLibrarianById(Long id) {
        if (librarianRepository.findById(id).isPresent()) {
            return librarianRepository.findById(id).orElse(null);
        }
        return null;
    }

    public List<Librarian> getLibrarians() {
        return librarianRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Librarian librarian = (Librarian) librarianRepository.
                findByUsername(username).orElseThrow(() -> new
                        UsernameNotFoundException("Librarian not found with username: " + username));
        return new org.springframework.security.core.
                userdetails.User(librarian.getUsername(), librarian.getPassword(), List.of());
    }
}

package ru.home.rediscashe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.home.rediscashe.entity.Author;
import ru.home.rediscashe.service.AuthorService;
import ru.home.rediscashe.service.ManualAuthorService;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;
    private final ManualAuthorService manualAuthorService;

    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(authorService.createAuthor(author));
                .body(manualAuthorService.createAuthor(author));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getById(@PathVariable Long id) {
//        return ResponseEntity.ok(authorService.getById(id));
        return ResponseEntity.ok(manualAuthorService.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Author>> getByLastName(
            @RequestParam String lastName) {
//        return ResponseEntity.ok(authorService.getByLastName(lastName));
        return ResponseEntity.ok(manualAuthorService.getByLastName(lastName));
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
//        return ResponseEntity.ok(authorService.getAllAuthors());
        return ResponseEntity.ok(manualAuthorService.getAllAuthors());
    }
}

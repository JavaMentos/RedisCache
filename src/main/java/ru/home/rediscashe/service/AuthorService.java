package ru.home.rediscashe.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.home.rediscashe.entity.Author;
import ru.home.rediscashe.repository.AuthorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Cacheable(value = "authorsById", key = "#id")
    public Author getById(Long id) {
        log.info("Fetching author from DB by ID: {}", id);
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found"));
    }

    @Cacheable(value = "authorsByLastName", key = "#lastName")
    public List<Author> getByLastName(String lastName) {
        log.info("Fetching authors from DB by last name: {}", lastName);
        return authorRepository.findByLastName(lastName);
    }

    //Очистка кеша, чтобы не получать устаревшие данные
    @CacheEvict(value = {"authorsById", "authorsByLastName", "allAuthors"}, allEntries = true)
    public Author createAuthor(Author author) {
        log.info("Saving new author: {}", author);
        return authorRepository.save(author);
    }

    @Cacheable("allAuthors")
    public List<Author> getAllAuthors() {
        log.info("Fetching all authors");
        return authorRepository.findAllCached();
    }
}

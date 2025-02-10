package ru.home.rediscashe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.home.rediscashe.entity.Author;
import ru.home.rediscashe.repository.AuthorRepository;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManualAuthorService {
    private final AuthorRepository authorRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.cache.redis.time-to-live}")
    private long ttl;

    public Author getById(Long id) {
        String key = "author:id:" + id;

        Author cachedAuthor = (Author) redisTemplate.opsForValue().get(key);
        if (cachedAuthor != null) {
            log.info("Returning cached author by ID: {}", id);
            return cachedAuthor;
        }

        log.info("Fetching author from DB by ID: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        redisTemplate.opsForValue().set(key, author, Duration.ofMillis(ttl));
        return author;
    }

    public List<Author> getByLastName(String lastName) {
        String key = "author:lastName:" + lastName;

        List<Author> cachedAuthors = (List<Author>) redisTemplate.opsForValue().get(key);
        if (cachedAuthors != null) {
            log.info("Returning cached authors by last name: {}", lastName);
            return cachedAuthors;
        }

        log.info("Fetching authors from DB by last name: {}", lastName);
        List<Author> authors = authorRepository.findByLastName(lastName);

        redisTemplate.opsForValue().set(key, authors, Duration.ofMillis(ttl));
        return authors;
    }

    public Author createAuthor(Author author) {
        log.info("Saving new author: {}", author);
        Author savedAuthor = authorRepository.save(author);

        // очистка кешей
        redisTemplate.delete("author:id:" + savedAuthor.getId());
        redisTemplate.delete("author:lastName:" + savedAuthor.getLastName());
        redisTemplate.delete("authors:all");

        return savedAuthor;
    }

    public List<Author> getAllAuthors() {
        String key = "authors:all";

        List<Author> cachedAuthors = (List<Author>) redisTemplate.opsForValue().get(key);
        if (cachedAuthors != null) {
            log.info("Returning all cached authors");
            return cachedAuthors;
        }

        log.info("Fetching all authors from DB");
        List<Author> authors = authorRepository.findAll();

        redisTemplate.opsForValue().set(key, authors, Duration.ofMillis(ttl));
        return authors;
    }
}

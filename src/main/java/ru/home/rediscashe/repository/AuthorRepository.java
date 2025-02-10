package ru.home.rediscashe.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.home.rediscashe.entity.Author;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
//    @Cacheable("authorsByLastName")
    List<Author> findByLastName(String lastName);

//    @Cacheable("allAuthors")
    @Query("SELECT a FROM Author a")
    List<Author> findAllCached();
}

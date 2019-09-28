package com.zhitar.library.repository;

import com.zhitar.library.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByOwnerId(Integer userId);

    @Query("SELECT b FROM Book b JOIN FETCH b.authors JOIN FETCH b.attributes WHERE b.id=?1")
    Book findByIdWithAuthorsAndAttributes(Integer id);

    @Query("SELECT b FROM Book b")
    Page<Book> findAll(Pageable pageable);

    @Query("SELECT DISTINCT b FROM Book b JOIN FETCH b.authors WHERE b.id IN (:bookIds)")
    List<Book> findByIdsWithAuthor(@Param("bookIds") List<Integer> bookIds);
}

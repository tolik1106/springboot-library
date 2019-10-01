package com.zhitar.library.repository;

import com.zhitar.library.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Query("SELECT DISTINCT b FROM Book b JOIN FETCH b.authors WHERE b.id IN (:bookIds) ORDER BY b.name")
    List<Book> findByIdsWithAuthor(@Param("bookIds") List<Integer> bookIds, Sort sort);

    @Query(value = "SELECT b FROM Book b WHERE b.name LIKE %:name%",
            countQuery = "SELECT COUNT(b) FROM Book b WHERE b.name LIKE %:name%")
    Page<Book> findByName(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT b FROM Book b JOIN b.authors a WHERE a.name = :authorName")
    Page<Book> findByAuthorsAuthorName(@Param("authorName") String authorName, Pageable pageable);

    @Query(value = "SELECT b FROM Book b JOIN b.attributes attr WHERE attr.name = :attributeName")
    Page<Book> findByAttributeAttributeName(@Param("attributeName") String attributeName, Pageable pageable);
}

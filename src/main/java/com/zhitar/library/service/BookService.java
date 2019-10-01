package com.zhitar.library.service;

import com.zhitar.library.dto.BookDto;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.Collection;
import java.util.List;

public interface BookService {

    Page<BookDto> findByAuthor(String authorName, Pageable pageable);

    Page<BookDto> findByNameWithAuthor(String name, Pageable pageable);

    Page<BookDto> findByAttribute(String attribute, Pageable pageable);

    BookDto takeBook(Integer bookId, Integer userId);

    Page<BookDto> findAll(Pageable pageable);

    Collection<BookDto> findByUser(Integer userId);

    BookDto cancelOrder(Integer bookId, Integer userId);

}

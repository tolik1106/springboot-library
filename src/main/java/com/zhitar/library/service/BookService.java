package com.zhitar.library.service;

import com.zhitar.library.dto.BookDto;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.Collection;
import java.util.List;

public interface BookService {

    List<BookDto> findByAuthor(String authorName);

    List<BookDto> findByNameWithAuthor(String name);

    List<BookDto> findByAttribute(String attribute);

    BookDto takeBook(Integer bookId, Integer userId);

    Page<BookDto> findAll(Pageable pageable);

    Collection<BookDto> findByUser(Integer userId);

    BookDto cancelOrder(Integer bookId, Integer userId);

}

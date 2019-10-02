package com.zhitar.library.service;

import com.zhitar.library.domain.Attribute;
import com.zhitar.library.domain.Author;
import com.zhitar.library.dto.BookDto;
import com.zhitar.library.dto.UserDto;

import java.util.List;

public interface AdminService {

    void deleteBook(Integer id);

    BookDto findBook(Integer id);

    BookDto save(BookDto bookDto);

    BookDto saveAuthor(Integer bookId, Author author);

    BookDto saveAttribute(Integer bookId, Attribute attribute);

    BookDto giveBook(Integer userId, Integer bookId);

    BookDto update(BookDto bookDto);

    List<UserDto> findUsersWithBook();

    BookDto returnBook(Integer bookId, Integer userId);

    void deleteAttribute(Integer bookId, String attribute);

    void deleteAuthor(Integer bookId, String author);
}

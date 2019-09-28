package com.zhitar.library.service.impl;

import com.zhitar.library.domain.Book;
import com.zhitar.library.domain.User;
import com.zhitar.library.dto.BookDto;
import com.zhitar.library.dto.UserDto;
import com.zhitar.library.mapper.BookMapper;
import com.zhitar.library.repository.BookRepository;
import com.zhitar.library.repository.UserRepository;
import com.zhitar.library.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;

    @Override
    public void deleteBook(Integer id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BookDto findBook(Integer id) {
        Book book = bookRepository.findByIdWithAuthorsAndAttributes(id);
        BookDto bookDto = bookMapper.toDto(book);
        bookDto.setAuthors(book.getAuthors());
        bookDto.setAttributes(book.getAttributes());
        return bookDto;
    }

    @Override
    public BookDto save(BookDto bookDto) {

        return null;
    }

    @Override
    public BookDto giveBook(Integer userId, Integer bookId) {
        return null;
    }

    @Override
    public BookDto update(BookDto bookDto) {
        return null;
    }

    @Override
    public List<UserDto> findUsersWithBook() {
        List<User> allWithBooks = userRepository.findAllWithBooks();
        return null;
    }

    @Override
    public BookDto returnBook(Integer bookId, Integer userId) {
        return null;
    }
}

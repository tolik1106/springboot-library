package com.zhitar.library.service.impl;

import com.zhitar.library.domain.Book;
import com.zhitar.library.domain.User;
import com.zhitar.library.dto.BookDto;
import com.zhitar.library.exception.NotFoundException;
import com.zhitar.library.mapper.BookMapper;
import com.zhitar.library.repository.BookRepository;
import com.zhitar.library.repository.UserRepository;
import com.zhitar.library.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;

    @Override
    public List<BookDto> findByAuthor(String authorName) {
        return null;
    }

    @Override
    public List<BookDto> findByNameWithAuthor(String name) {
        return null;
    }

    @Override
    public List<BookDto> findByAttribute(String attribute) {
        return null;
    }

    @Override
    @Transactional
    public BookDto takeBook(Integer bookId, Integer userId) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new NotFoundException("Book with id " + bookId + " not found")
        );
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id " + userId + " not found")
        );
        book.setOwner(user);
        book.setOwnedDate(new Date());
        book.setOrdered(true);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        List<Book> bookList = bookRepository.findByIdsWithAuthor(
                books.getContent().stream().map(Book::getId).collect(Collectors.toList())
        );
        books = new PageImpl<Book>(bookList, books.getPageable(), bookList.size());
        Page<BookDto> bookDtos = books.map((book) -> {
            BookDto bookDto = bookMapper.toDto(book);
            bookDto.setAuthors(book.getAuthors());
            return bookDto;
        });
        return bookDtos;
    }

    @Override
    public Collection<BookDto> findByUser(Integer userId) {
        List<Book> books = bookRepository.findByOwnerId(userId);
        return null;
    }

    @Override
    @Transactional
    public BookDto cancelOrder(Integer bookId, Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id " + userId + " not found")
        );
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new NotFoundException("Book with id " + bookId + " not found")
        );
        if (!book.getOrdered()) {
            throw new NotFoundException("This book isn't ordered");
        }
        book.setOwner(null);
        book.setOwnedDate(null);
        book.setOrdered(false);
        return bookMapper.toDto(bookRepository.save(book));
    }
}

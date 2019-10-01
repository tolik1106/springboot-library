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
import org.springframework.data.domain.Sort;
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
    public Page<BookDto> findByAuthor(String authorName, Pageable pageable) {
        Page<Book> books = bookRepository.findByAuthorsAuthorName(authorName, pageable);
        if (books.isEmpty()) {
            return getBookDtosPage(books);
        }
        books = getBooksPage(books);
        return getBookDtosPage(books);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookDto> findByNameWithAuthor(String name, Pageable pageable) {
        Page<Book> books = bookRepository.findByName(name, pageable);
        if (books.isEmpty()) {
            return getBookDtosPage(books);
        }
        books = getBooksPage(books);
        return getBookDtosPage(books);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookDto> findByAttribute(String attribute, Pageable pageable) {
        Page<Book> books = bookRepository.findByAttributeAttributeName(attribute, pageable);
        if (books.isEmpty()) {
            return getBookDtosPage(books);
        }
        books = getBooksPage(books);
        return getBookDtosPage(books);
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
        if (books.isEmpty()) {
            return getBookDtosPage(books);
        }
        books = getBooksPage(books);
        return getBookDtosPage(books);
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

    private Page<Book> getBooksPage(Page<Book> books) {
        List<Book> bookList = bookRepository.findByIdsWithAuthor(
                books.getContent().stream().map(Book::getId).collect(Collectors.toList()),
                new Sort(Sort.Direction.ASC, "name")
        );
        books = new PageImpl<>(bookList, books.getPageable(), books.getTotalElements());
        return books;
    }

    private Page<BookDto> getBookDtosPage(Page<Book> books) {
        return books.map((book) -> {
            BookDto bookDto = bookMapper.toDto(book);
            bookDto.setAsStringAuthors(book.getAuthors());
            return bookDto;
        });
    }
}

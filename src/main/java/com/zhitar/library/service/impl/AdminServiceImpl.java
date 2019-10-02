package com.zhitar.library.service.impl;

import com.zhitar.library.domain.Attribute;
import com.zhitar.library.domain.Author;
import com.zhitar.library.domain.Book;
import com.zhitar.library.domain.User;
import com.zhitar.library.dto.BookDto;
import com.zhitar.library.dto.UserDto;
import com.zhitar.library.exception.NotFoundException;
import com.zhitar.library.mapper.BookMapper;
import com.zhitar.library.mapper.UserMapper;
import com.zhitar.library.repository.AttributeRepository;
import com.zhitar.library.repository.AuthorRepository;
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
    private final AuthorRepository authorRepository;
    private final AttributeRepository attributeRepository;
    private final BookMapper bookMapper;
    private final UserMapper userMapper;

    @Override
    public void deleteBook(Integer id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BookDto findBook(Integer id) {
        Book book = bookRepository.findByIdWithAuthorsAndAttributes(id);
        BookDto bookDto = bookMapper.toDto(book);
        bookDto.setAsStringAuthors(book.getAuthors());
        bookDto.setAsStringAttributes(book.getAttributes());
        return bookDto;
    }

    @Override
    public BookDto save(BookDto bookDto) {
        Book bookToSave = bookMapper.toEntity(bookDto);
        return bookMapper.toDto(bookRepository.save(bookToSave));
    }

    @Override
    public BookDto giveBook(Integer userId, Integer bookId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book with id " + bookId + " not found"));
        book.setOrdered(false);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto update(BookDto bookDto) {
        Book bookFromDb = bookRepository.findById(bookDto.getId()).orElseThrow(() -> new NotFoundException("Book with id " + bookDto.getId() + " not found"));
        Book bookToUpdate = bookMapper.toEntity(bookDto);
        return bookMapper.toDto(bookRepository.save(bookToUpdate));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findUsersWithBook() {
        List<User> allWithBooks = userRepository.findAllWithBooks();
        return userMapper.toDto(allWithBooks);
    }

    @Override
    public BookDto returnBook(Integer bookId, Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book with id " + bookId + " not found"));
        book.setOwner(null);
        book.setOwnedDate(null);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteAttribute(Integer bookId, String attribute) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book with id " + bookId + " not found"));
        Attribute attributeFromDb = attributeRepository.findByName(attribute).orElseThrow(() -> new NotFoundException("Attribute " + attribute + " not found"));
        book.getAttributes().remove(attributeFromDb);
    }

    @Override
    public void deleteAuthor(Integer bookId, String author) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book with id " + bookId + " not found"));
        Author authorFromDb = authorRepository.findByName(author).orElseThrow(() -> new NotFoundException("Author " + author + " not found"));
        book.getAuthors().remove(authorFromDb);
    }

    @Override
    public BookDto saveAuthor(Integer bookId, Author author) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book with id " + bookId + " not found"));
        Author authorFromDb = authorRepository.findByName(author.getName()).orElseGet(() -> authorRepository.save(author));
        book.getAuthors().add(authorFromDb);
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto saveAttribute(Integer bookId, Attribute attribute) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book with id " + bookId + " not found"));
        Attribute attributeFromDb = attributeRepository.findByName(attribute.getName()).orElseGet(() -> attributeRepository.save(attribute));
        book.getAttributes().add(attributeFromDb);
        return bookMapper.toDto(book);
    }
}

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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        saveNewAuthors(bookDto, bookToSave);

        saveNewAttributes(bookDto, bookToSave);
        return bookMapper.toDto(bookRepository.save(bookToSave));
    }

    private void saveNewAttributes(BookDto bookDto, Book book) {
        List<Attribute> bookAttributes = getList(bookDto.getAttributes(), Attribute::new);
        List<Attribute> allAttributes = attributeRepository.findAll();
        List<Attribute> toSaveAttr = new ArrayList<>();
        List<Attribute> existAttr = new ArrayList<>();
        for (Attribute bookAttribute : bookAttributes) {
            boolean isNew = true;
            for (Attribute attribute : allAttributes) {
                if (bookAttribute.getName().equalsIgnoreCase(attribute.getName())) {
                    bookAttribute.setId(attribute.getId());
                    isNew = false;
                    break;
                }
            }
            if (isNew) {
                toSaveAttr.add(bookAttribute);
            } else {
                existAttr.add(bookAttribute);
            }
        }
        List<Attribute> savedAttr = attributeRepository.saveAll(toSaveAttr);
        book.setAttributes(new HashSet<>(existAttr));
        book.getAttributes().addAll(savedAttr);
    }

    private void saveNewAuthors(BookDto bookDto, Book book) {
        List<Author> bookAuthors = getList(bookDto.getAuthors(), Author::new);
        List<Author> allAuthors = authorRepository.findAll();
        List<Author> toSaveAuth = new ArrayList<>();
        List<Author> existsAuth = new ArrayList<>();
        for (Author bookAuthor : bookAuthors) {
            boolean isNew = true;
            for (Author author : allAuthors) {
                if (bookAuthor.getName().equalsIgnoreCase(author.getName())) {
                    bookAuthor.setId(author.getId());
                    isNew = false;
                    break;
                }
            }
            if (isNew) {
                toSaveAuth.add(bookAuthor);
            } else {
                existsAuth.add(bookAuthor);
            }
        }
        List<Author> saved = authorRepository.saveAll(toSaveAuth);
        book.setAuthors(new HashSet<>(existsAuth));
        book.getAuthors().addAll(saved);
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

        saveNewAuthors(bookDto, bookToUpdate);

        saveNewAttributes(bookDto, bookToUpdate);
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

    private <T> List<T> getList(String values, Function<String, T> mapper) {
        String[] split = values.split(",");

        List<String> names = Arrays.stream(split).map(String::trim).collect(Collectors.toList());
        TreeSet<String> seen = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        names.removeIf(name -> !seen.add(name));

        return names.stream().map(mapper).collect(Collectors.toList());
    }
}

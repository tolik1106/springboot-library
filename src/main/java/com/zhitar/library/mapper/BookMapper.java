package com.zhitar.library.mapper;

import com.zhitar.library.domain.Book;
import com.zhitar.library.domain.User;
import com.zhitar.library.dto.BookDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class BookMapper implements EntityMapper<BookDto, Book> {
    @Override
    public Book toEntity(BookDto bookDto) {
        return new Book(
                bookDto.getId(),
                bookDto.getOwnerId() == null ? null : createOwner(bookDto.getOwnerId()),
                bookDto.getName(),
                null,
                bookDto.getBookcase(),
                bookDto.getBookshelf(),
                bookDto.getOrdered()
        );
    }

    @Override
    public BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getOwner() == null ? null : book.getOwner().getId(),
                book.getName(),
                getExpiredDays(book.getOwnedDate()),
                book.getBookcase(),
                book.getBookshelf(),
                book.getOrdered()
        );
    }

    @Override
    public List<BookDto> toDto(List<Book> books) {
        if (books == null) {
            return null;
        }
        List<BookDto> dtos = new ArrayList<>();
        for (Book book : books) {
            dtos.add(toDto(book));
        }
        return dtos;
    }

    @Override
    public List<Book> toEntity(List<BookDto> dtos) {
        if (dtos == null) {
            return null;
        }
        List<Book> books = new ArrayList<>();
        for (BookDto dto : dtos) {
            books.add(toEntity(dto));
        }
        return books;
    }

    private Integer getExpiredDays(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        return (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    private User createOwner(Integer id) {
        User user = new User();
        user.setId(id);
        return user;
    }
}

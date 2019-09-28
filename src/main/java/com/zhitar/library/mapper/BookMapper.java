package com.zhitar.library.mapper;

import com.zhitar.library.domain.Book;
import com.zhitar.library.dto.BookDto;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class BookMapper implements EntityMapper<BookDto, Book> {
    @Override
    public Book toEntity(BookDto bookDto) {
        return null;
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
                book.getOrdered(),
                null,
                null
        );
    }

    private Integer getExpiredDays(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        return (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}

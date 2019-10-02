package com.zhitar.library.dto;

import com.zhitar.library.domain.Attribute;
import com.zhitar.library.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private Integer id;
    private Integer ownerId;
    @NotBlank
    @Size(min = 3, max = 100)
    private String name;
    private Integer expiredDays;
    @Range(min = 1L, max = 20L)
    private Integer bookcase;
    @Range(min = 1L, max = 10L)
    private Integer bookshelf;
    private Boolean ordered = false;
    @Size(min = 2)
    private String authors;
    @Size(min = 2)
    private String attributes;

    public BookDto(Integer id, Integer ownerId, String name, Integer expiredDays, Integer bookcase, Integer bookshelf, Boolean ordered) {
        this(id, ownerId, name, expiredDays, bookcase, bookshelf, ordered, null, null);
    }

    public void setAsStringAuthors(Set<Author> authors) {
        if (authors != null && !authors.isEmpty()) {
            this.authors = authors.stream().map(Author::getName).collect(Collectors.joining(", "));
        }
    }

    public void setAsStringAttributes(Set<Attribute> attributes) {
        if (attributes != null && !attributes.isEmpty()) {
            this.attributes = attributes.stream().map(Attribute::getName).collect(Collectors.joining(", "));
        }
    }
}

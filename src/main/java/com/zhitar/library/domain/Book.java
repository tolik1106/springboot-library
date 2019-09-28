package com.zhitar.library.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Book extends AbstractEntity<Integer> {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_book_users"))
    private User owner;
    @Column(name = "name", length = 100, nullable = false)
    @NotBlank
    @Size(min = 3, max = 100)
    private String name;

    private Date ownedDate;

    @Column(nullable = false)
    private Integer bookcase;
    @Column(nullable = false)
    private Integer bookshelf;
    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean ordered;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "book_author", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new LinkedHashSet<>();
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "book_attribute", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "attribute_id"))
    private Set<Attribute> attributes = new LinkedHashSet<>();
}

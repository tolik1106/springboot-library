package com.zhitar.library.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "unique_author_name", columnNames = "name"))
public class Author extends AbstractEntity<Integer> {

    @NotBlank
    @Size(min = 3, max = 255)
    @Column(nullable = false)
    private String name;
}

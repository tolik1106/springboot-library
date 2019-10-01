package com.zhitar.library.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

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

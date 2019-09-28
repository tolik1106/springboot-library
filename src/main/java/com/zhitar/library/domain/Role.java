package com.zhitar.library.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "role", uniqueConstraints = @UniqueConstraint(columnNames = "role", name = "unique_role"))
public class Role extends AbstractEntity<Integer> implements GrantedAuthority {

    @NotBlank
    @Size(min = 3, max = 30)
    @Column(name = "role", nullable = false, length = 30)
    private String role;

    @Override
    public String getAuthority() {
        return /*"ROLE_" + */role;
    }
}

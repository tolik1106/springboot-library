package com.zhitar.library.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email", name = "unique_user_email"))
public class User extends AbstractEntity<Integer> implements UserDetails {

    @Column(name = "name", length = 64, nullable = false)
    @NotBlank
    @Size(min = 2, max = 64)
    private String name;
    @Email
    @NotBlank
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", nullable = false, length = 64)
    @NotBlank
    @Size(min = 5, max = 64)
    private String password;
    @Column(name = "phone", nullable = false, length = 10)
    @NotBlank
    @Pattern(regexp = "^0(95|50|66|99|67|96|97|98|63|93|73)\\d{7}")
    private String phone;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    private List<Book> books;
    public User(Integer id, String name, String email, String password, String phone, Role... roles) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.roles.addAll(Arrays.asList(roles));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

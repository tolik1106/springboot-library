package com.zhitar.library.repository;

import com.zhitar.library.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email=:email")
    User findByEmailWithRoles(@Param("email") String email);

    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.books")
    List<User> findAllWithBooks();
}

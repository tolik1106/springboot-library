package com.zhitar.library.repository;

import com.zhitar.library.domain.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttributeRepository extends JpaRepository<Attribute, Integer> {

    Optional<Attribute> findByName(String name);
}

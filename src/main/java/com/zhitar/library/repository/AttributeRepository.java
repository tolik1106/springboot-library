package com.zhitar.library.repository;

import com.zhitar.library.domain.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeRepository extends JpaRepository<Attribute, Integer> {
}

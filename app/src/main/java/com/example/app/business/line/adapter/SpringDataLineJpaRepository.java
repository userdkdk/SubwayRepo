package com.example.app.business.line.adapter;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataLineJpaRepository extends JpaRepository<LineJpaEntity, Integer> {
    boolean existsByName(String name);
}

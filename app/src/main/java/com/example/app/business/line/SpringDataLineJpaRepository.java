package com.example.app.business.line;

import com.example.core.common.domain.enums.ActiveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SpringDataLineJpaRepository extends JpaRepository<LineJpaEntity, Integer> {
    boolean existsByName(String name);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update LineJpaEntity l " +
            "set l.activeType = :active " +
            "where l.name = :name")
    int setActivateByName(String name, ActiveType activeType);
}

package com.example.db.business.line;

import com.example.core.common.domain.enums.ActiveType;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SpringDataLineJpaRepository extends JpaRepository<LineJpaEntity, Integer> {
    List<LineJpaEntity> findByActiveType(ActiveType activeType);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update LineJpaEntity l " +
            "set l.activeType = :toActiveType " +
            "where l.id = :id " +
            "and l.activeType = :activeType")
    int setActivateById(Integer id, ActiveType activeType, ActiveType toActiveType);

    boolean existsByIdAndActiveType(Integer id, ActiveType activeType);

    // test
    int countByName(String name);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("select l from LineJpaEntity l where l.id = :id")
    Optional<LineJpaEntity> findByIdForUpdate(@Param("id") Integer id);
}

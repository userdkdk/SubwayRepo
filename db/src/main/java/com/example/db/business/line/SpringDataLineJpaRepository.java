package com.example.db.business.line;

import com.example.core.common.domain.enums.ActiveType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringDataLineJpaRepository extends JpaRepository<LineJpaEntity, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from LineJpaEntity l where l.id = :id")
    Optional<LineJpaEntity> findByIdForUpdate(@Param("id") Integer id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update LineJpaEntity l " +
            "set l.activeType = :toActiveType " +
            "where l.id = :id " +
            "and l.activeType = :activeType")
    int setActivateById(
            @Param("id")  Integer id,
            @Param("activeType") ActiveType activeType,
            @Param("toActiveType") ActiveType toActiveType);

    boolean existsByIdAndActiveType(
            @Param("id") Integer id,
            @Param("activeType") ActiveType activeType);

    List<LineJpaEntity> findByActiveType(ActiveType activeType);



    // test
    int countByName(String name);
}

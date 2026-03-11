package com.example.core.domain.lineSnapshot;

import java.util.List;

public interface LineSnapshotRepository {
    Integer save(LineSnapshot lineSnapshot);

    List<Integer> findSegsIdByLine(Integer id);
}

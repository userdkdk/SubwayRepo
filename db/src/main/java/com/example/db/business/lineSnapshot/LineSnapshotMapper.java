package com.example.db.business.lineSnapshot;

import com.example.core.domain.lineSnapshot.LineSnapshot;
import org.springframework.stereotype.Component;

@Component
public class LineSnapshotMapper {
    public LineSnapshotJpaEntity toNewEntity(LineSnapshot lineSnapshot) {
        return LineSnapshotJpaEntity.create(lineSnapshot.getLineId());
    }
}

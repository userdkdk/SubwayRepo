package com.example.app.business.path;

import com.example.app.business.path.model.Path;
import com.example.app.business.path.model.PathEdge;
import com.example.app.business.path.model.PathResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class PathCalculatorTest {

    @Test
    @DisplayName("경로 없을시 not found")
    void noPathReturned() {
        Path path = stubPath(Map.of(
                1, List.of(edge(1, 2, 5)),
                2, List.of()
        ));

        PathResult result = PathCalculator.findByTime(path, 1, 3);

        assertThat(result.found()).isFalse();
    }

    @Test
    @DisplayName("최단 시간이 되는 경로를 선택하고, stations 경로가 올바르게 복원된다")
    void choosesShortestPath_andRebuildsStations() {
        // 1->2(10), 1->3(3), 3->2(3), 2->4(2), 3->4(100)
        // 최단: 1-3-2-4 : 3+3+2=8
        Path path = stubPath(Map.of(
                1, List.of(edge(2, 10, 10), edge(3, 21.58, 3)),
                2, List.of(edge(4, 0, 2)),
                3, List.of(edge(2, 19.323, 3), edge(4, 100, 100)),
                4, List.of()
        ));

        PathResult result = PathCalculator.findByTime(path, 1, 4);

        assertThat(result.found()).isTrue();
        assertThat(result.stations()).containsExactly(1,3,2,4);
    }

    @Test
    @DisplayName("직행이 있어도 우회가 더 빠르면 우회를 선택한다")
    void prefersDetourIfFaster() {
        // 1->4(50) 직행
        // 1->2(10), 2->3(10), 3->4(10) = 30
        Path path = stubPath(Map.of(
                1, List.of(edge(4, 50, 50), edge(2, 10, 10)),
                2, List.of(edge(3, 10, 10)),
                3, List.of(edge(4, 10, 10)),
                4, List.of()
        ));

        PathResult result = PathCalculator.findByTime(path, 1, 4);

        assertThat(result.found()).isTrue();
        assertThat(result.stations()).containsExactly(1, 2, 3, 4);
    }

    private static Path stubPath(Map<Integer, List<PathEdge>> graph) {
        return new Path(graph);
    }

    private static PathEdge edge(int to, double distance, int time) {
        return new PathEdge(to, distance, time);
        // ✅ 만약 PathEdge가 record/static factory라면 여기를 프로젝트 구현에 맞게 바꿔.
        // 예: PathEdge.of(from,to,time) / new PathEdge(to,time) 등
    }
}
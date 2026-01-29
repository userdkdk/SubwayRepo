package com.example.app.business.path;

import com.example.app.business.path.model.Path;
import com.example.app.business.path.model.PathEdge;
import com.example.app.business.path.model.PathResult;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class PathCalculator {
    public static PathResult findByTime(Path path, Integer startId, Integer endId) {
        if (Objects.equals(startId, endId)) {
            return PathResult.single(startId);
        }
        // total time
        Map<Integer, TimeDistance> dist = new HashMap<>();
        // max TIme
        TimeDistance maxTimeDist = new TimeDistance(Integer.MAX_VALUE,0);
        // prev station
        Map<Integer, Integer> prev = new HashMap<>();

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(Node::totalTime));
        dist.put(startId, new TimeDistance(0,0));
        pq.offer(new Node(startId, 0));

        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            int best = dist.getOrDefault(cur.stationId, maxTimeDist).totalTime;
            if (cur.totalTime != best) continue;
            if (cur.stationId == endId) break;

            for (PathEdge e : path.edgesOf(cur.stationId)) {
                int next = e.toStationId();
                int nextTotalTime = cur.totalTime + e.spendTime();
                int old = dist.getOrDefault(next, maxTimeDist).totalTime;

                if (nextTotalTime < old) {
                    double nextTotalDistance = dist.get(cur.stationId).totalDistance
                            + e.distance();
                    dist.put(next, new TimeDistance(nextTotalTime, nextTotalDistance));
                    prev.put(next, cur.stationId);
                    pq.offer(new Node(next, nextTotalTime));
                }
            }
        }

        Integer totalTime = dist.get(endId).totalTime;
        int totalDistance = Math.toIntExact(Math.round(dist.get(endId).totalDistance));
        if (totalTime==null) {
            return PathResult.notFound(startId, endId);
        }
        List<Integer> stations = rebuildPath(prev, startId, endId);

        return PathResult.found(startId, endId, stations,totalTime,totalDistance);
    }

    private static List<Integer> rebuildPath(Map<Integer, Integer> prev,
                                             int start, int end) {
        ArrayList<Integer> path = new ArrayList<>();
        Integer cur = end;
        path.addFirst(cur);

        while (cur != null && cur != start) {
            cur = prev.get(cur);
            if (cur==null) break;
            path.addFirst(cur);
        }

        if (path.getFirst() != start) {
            return List.of();
        }
        return List.copyOf(path);
    }

    private record Node(int stationId, int totalTime) {}
    private record TimeDistance(Integer totalTime, double totalDistance) {}
}

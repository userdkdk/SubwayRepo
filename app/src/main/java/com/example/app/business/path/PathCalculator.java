package com.example.app.business.path;

import com.example.app.business.path.model.Path;
import com.example.app.business.path.model.PathEdge;
import com.example.app.business.path.model.PathResult;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class PathCalculator {
    public static PathResult findTime(Path path, Integer startId, Integer endId) {
        if (Objects.equals(startId, endId)) {
            return PathResult.single(startId);
        }
        Map<Integer, Integer> dist = new HashMap<>();
        Map<Integer, Integer> prev = new HashMap<>();
        Map<Integer, PathEdge> prevEdge = new HashMap<>();

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(Node::totalTime));
        dist.put(startId, 0);
        pq.offer(new Node(startId, 0));

        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            int best = dist.getOrDefault(cur.stationId, Integer.MAX_VALUE);
            if (cur.totalTime != best) continue;
            if (cur.stationId == endId) break;

            for (PathEdge e : path.edgesOf(cur.stationId)) {
                int next = e.toStationId();
                int cand = cur.totalTime + e.spendTime();
                int old = dist.getOrDefault(next, Integer.MAX_VALUE);

                if (cand < old) {
                    dist.put(next, cand);
                    prev.put(next, cur.stationId);
                    prevEdge.put(next, e);
                    pq.offer(new Node(next, cand));
                }
            }
        }

        Integer totalTime = dist.get(endId);
        if (totalTime==null) {
            return PathResult.notFound(startId, endId);
        }
        List<Integer> stations = rebuildPath(prev, startId, endId);

        return PathResult.found(startId, endId, stations,totalTime,0);
    }

    private static List<Integer> rebuildPath(Map<Integer, Integer> prev, int start, int end) {
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
}

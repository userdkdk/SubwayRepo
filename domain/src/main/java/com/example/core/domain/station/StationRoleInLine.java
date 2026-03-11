package com.example.core.domain.station;

public enum StationRoleInLine {
    HEAD, TAIL, INTERNAL, NOT_IN_LINE;

    public static StationRoleInLine from (boolean hasBefore, boolean hasAfter) {
        if (hasBefore && hasAfter) {
            return INTERNAL;
        }
        if (!hasBefore && hasAfter) {
            return HEAD;
        }
        if (hasBefore && !hasAfter) {
            return TAIL;
        }
        return NOT_IN_LINE;
    }
}

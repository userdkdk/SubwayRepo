package com.example.core.business.line;

public interface LineRepository{
    Line upsertActivateByName(String name, Integer start, Integer end);
}

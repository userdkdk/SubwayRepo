package com.example.app.support;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DbCleaner {

    private final JdbcTemplate jdbcTemplate;
    public void truncateAll() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.execute("TRUNCATE TABLE `segment_histories`");
        jdbcTemplate.execute("TRUNCATE TABLE `segments`");
        jdbcTemplate.execute("TRUNCATE TABLE `line_stations`");
        jdbcTemplate.execute("TRUNCATE TABLE `lines`");
        jdbcTemplate.execute("TRUNCATE TABLE `stations`");
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");

    }
}

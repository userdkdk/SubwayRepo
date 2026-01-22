package com.example.app.api.line.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LineResponse {
    private final Integer id;
    @Schema(example = "tmptmp")
    private final String name;
}

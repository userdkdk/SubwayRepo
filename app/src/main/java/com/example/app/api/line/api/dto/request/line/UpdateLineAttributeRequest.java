package com.example.app.api.line.api.dto.request.line;

import jakarta.validation.constraints.NotBlank;

public record UpdateLineAttributeRequest(
        @NotBlank String name
) {}

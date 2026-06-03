package io.bandi.gamelens.batch.domain.dto;

import java.time.LocalDateTime;

public record BacklogServiceResponse(
        Long id,
        Long gameId,
        String status,
        Integer priority,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

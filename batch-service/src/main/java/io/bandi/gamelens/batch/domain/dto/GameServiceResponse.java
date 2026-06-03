package io.bandi.gamelens.batch.domain.dto;

import java.time.LocalDateTime;

public record GameServiceResponse(
        Long id,
        Long rawgId,
        String title,
        String coverUrl,
        Integer averagePlaytime,
        Double rating,
        String platform,
        LocalDateTime createdAt
) {
}

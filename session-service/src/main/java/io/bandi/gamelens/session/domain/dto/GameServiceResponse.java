package io.bandi.gamelens.session.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GameServiceResponse(
        Long id,
        Long rawgId,
        String title,
        String coverUrl,
        Integer averagePlaytime,
        BigDecimal rating,
        String platform,
        LocalDateTime createdAt
) {
}

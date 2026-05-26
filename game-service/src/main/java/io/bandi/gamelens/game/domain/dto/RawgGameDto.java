package io.bandi.gamelens.game.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RawgGameDto(
        long id,
        String name,
        String backgroundImage,
        BigDecimal rating,
        int playtime,
        LocalDate released,
        List<RawgPlatformWrapper> platforms
) {
}

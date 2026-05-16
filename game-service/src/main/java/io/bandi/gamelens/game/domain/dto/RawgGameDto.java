package io.bandi.gamelens.game.domain.dto;

import java.math.BigDecimal;
import java.util.List;

public record RawgGameDto(
        long id,
        String name,
        String backgroundImage,
        BigDecimal rating,
        int playtime,
        List<RawgPlatformWrapper> platforms
) {
}

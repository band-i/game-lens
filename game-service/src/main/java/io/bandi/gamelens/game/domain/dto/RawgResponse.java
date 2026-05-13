package io.bandi.gamelens.game.domain.dto;

import java.util.List;

public record RawgResponse(
        int count,
        List<RawgGameDto> results
) {
}

package io.bandi.gamelens.backlog.domain.dto;

import io.bandi.gamelens.backlog.domain.model.Status;

public record BacklogRequest(
        Status status,
        Integer priority
) {
}

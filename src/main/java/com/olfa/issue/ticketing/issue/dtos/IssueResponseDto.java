package com.olfa.issue.ticketing.issue.dtos;

import com.olfa.issue.ticketing.issue.enumerations.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record IssueResponseDto(
        UUID id,
        String description,
        String category,
        int priority,
        int progress,
        Status status,
        boolean active,
        LocalDateTime creationDate
) {
}

package com.olfa.issue.ticketing.issue.dtos;

import com.olfa.issue.ticketing.issue.enumerations.Category;
import com.olfa.issue.ticketing.issue.enumerations.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record IssueResponseDto(
        UUID id,
        String title,
        String description,
        Category category,
        int priority,
        int progress,
        Status status,
        boolean active,
        LocalDateTime creationDate,
        LocalDateTime updatedTime
) {
}

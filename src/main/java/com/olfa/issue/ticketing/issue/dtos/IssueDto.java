package com.olfa.issue.ticketing.issue.dtos;

import com.olfa.issue.ticketing.issue.entities.Issue;
import com.olfa.issue.ticketing.issue.enumerations.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record IssueDto(
        UUID id,
        String description,
        String category,
        int priority,
        int progress,
        Status status,
        boolean active,
        LocalDateTime createdAt
) {
    public static Issue toIssue(IssueDto issueDto) {
        return Issue.builder()
                .id(issueDto.id)
                .description(issueDto.description)
                .category(issueDto.category)
                .priority(issueDto.priority)
                .progress(issueDto.progress)
                .status(issueDto.status)
                .active(issueDto.active)
                .createdAt(issueDto.createdAt)
                .build();
    }
}

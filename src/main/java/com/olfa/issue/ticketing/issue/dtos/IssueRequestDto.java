package com.olfa.issue.ticketing.issue.dtos;

import com.olfa.issue.ticketing.issue.entities.Issue;
import com.olfa.issue.ticketing.issue.enumerations.Status;

public record IssueRequestDto(
        String description,
        String category,
        int priority,
        int progress,
        Status status,
        boolean active
) {
    public static Issue toIssue(IssueRequestDto issueRequestDto) {
        return Issue.builder()
                .description(issueRequestDto.description)
                .category(issueRequestDto.category)
                .priority(issueRequestDto.priority)
                .progress(issueRequestDto.progress)
                .status(issueRequestDto.status)
                .active(issueRequestDto.active)
                .build();
    }
}

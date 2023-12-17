package com.olfa.issue.ticketing.issue.dtos;

import com.olfa.issue.ticketing.issue.entities.Issue;
import com.olfa.issue.ticketing.issue.enumerations.Category;
import com.olfa.issue.ticketing.issue.enumerations.Status;

public record IssueRequestDto(
        String title,
        String description,
        Category category,
        int priority,
        int progress,
        Status status,
        boolean active
) {
    public static Issue toIssue(IssueRequestDto issueRequestDto) {
        return Issue.builder()
                .title(issueRequestDto.title)
                .description(issueRequestDto.description)
                .category(issueRequestDto.category)
                .priority(issueRequestDto.priority)
                .progress(issueRequestDto.progress)
                .status(issueRequestDto.status)
                .active(issueRequestDto.active)
                .build();
    }
}

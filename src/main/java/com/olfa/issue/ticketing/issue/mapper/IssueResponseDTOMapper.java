package com.olfa.issue.ticketing.issue.mapper;

import com.olfa.issue.ticketing.issue.dtos.IssueResponseDto;
import com.olfa.issue.ticketing.issue.entities.Issue;

import java.util.function.Function;

public class IssueResponseDTOMapper implements Function<Issue, IssueResponseDto> {

    @Override
    public IssueResponseDto apply(Issue issue) {
        return new IssueResponseDto(
                issue.getId(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getCategory(),
                issue.getPriority(),
                issue.getProgress(),
                issue.getStatus(),
                issue.isActive(),
                issue.getCreatedAt(),
                issue.getUpdatedAt()
        );
    }
}

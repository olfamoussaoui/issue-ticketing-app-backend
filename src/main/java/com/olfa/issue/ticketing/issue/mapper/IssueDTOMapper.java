package com.olfa.issue.ticketing.issue.mapper;

import com.olfa.issue.ticketing.issue.dtos.IssueDto;
import com.olfa.issue.ticketing.issue.entities.Issue;

import java.util.function.Function;

public class IssueDTOMapper implements Function<Issue, IssueDto> {

    @Override
    public IssueDto apply(Issue issue) {
        return new IssueDto(
                issue.getId(),
                issue.getDescription(),
                issue.getCategory(),
                issue.getPriority(),
                issue.getProgress(),
                issue.getStatus(),
                issue.isActive(),
                issue.getCreatedAt()
        );
    }
}

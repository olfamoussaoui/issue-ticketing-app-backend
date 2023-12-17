package com.olfa.issue.ticketing.issue.mapper;

import com.olfa.issue.ticketing.issue.entities.Issue;
import com.olfa.issue.ticketing.issue.enumerations.Category;
import com.olfa.issue.ticketing.issue.enumerations.Status;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IssueResponseDTOMapperTest {
    private final IssueResponseDTOMapper issueResponseDTOMapper = new IssueResponseDTOMapper();

    @Test
    void testIssueResponseDTOMapperPassIssueReturnsIssueResponseDTO() {
        final Issue issue = new Issue(
                UUID.randomUUID(),
                "Some problem with the PC",
                "Hardware",
                Category.HARDWARE,
                3,
                0,
                Status.OPEN,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        var mappedIssue = issueResponseDTOMapper.apply(issue);
        assertAll(
                () -> assertNotNull(mappedIssue),
                () -> assertSame(issue.getId(), mappedIssue.id()),
                () -> assertSame(issue.getTitle(), mappedIssue.title()),
                () -> assertSame(issue.getDescription(), mappedIssue.description()),
                () -> assertSame(issue.getCategory(), mappedIssue.category()),
                () -> assertSame(issue.getPriority(), mappedIssue.priority()),
                () -> assertSame(issue.getProgress(), mappedIssue.progress()),
                () -> assertSame(issue.getStatus(), mappedIssue.status()),
                () -> assertSame(issue.isActive(), mappedIssue.active()),
                () -> assertSame(issue.getCreatedAt(), mappedIssue.creationDate()),
                () -> assertSame(issue.getUpdatedAt(), mappedIssue.updatedTime())
        );
    }
}
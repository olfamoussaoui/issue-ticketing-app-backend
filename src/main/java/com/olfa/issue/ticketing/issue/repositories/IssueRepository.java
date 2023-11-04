package com.olfa.issue.ticketing.issue.repositories;

import com.olfa.issue.ticketing.issue.entities.Issue;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public interface IssueRepository {
    Issue createIssue(Issue issue);

    IssueResult updateIssue(Issue issue);

    IssueResult getIssue(UUID issueId);

    Collection<Issue> getAllIssues();

    Collection<Issue> getAllIssuesByStatus(Issue.Status status);

    Collection<Issue> getAllIssuesByRangeDate(LocalDate startDate, LocalDate endDate);

    Collection<Issue> getAllIssuesByPriority(Issue.Priority priority);

    IssueResult deleteIssueById(UUID issueId);

    sealed interface IOError {
        record IssueNotFoundError() implements IOError {
        }
    }
}

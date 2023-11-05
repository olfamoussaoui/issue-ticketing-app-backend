package com.olfa.issue.ticketing.issue.repositories;

import com.olfa.issue.ticketing.issue.entities.Issue;
import com.olfa.issue.ticketing.issue.enumerations.Status;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public interface IssueRepository {
    Issue createIssue(Issue issue);

    IssueResult updateIssue(Issue issue);

    IssueResult getIssue(UUID issueId);

    Collection<Issue> getAllIssues();

    Collection<Issue> getAllIssuesByStatus(Status status);

    Collection<Issue> getAllIssuesByRangeDate(LocalDate startDate, LocalDate endDate);

    Collection<Issue> getAllIssuesByPriority(int priority);

    IssueResult deleteIssueById(UUID issueId);
}

package com.olfa.issue.ticketing.issue.repositories;

import com.olfa.issue.ticketing.issue.entities.Issue;
import com.olfa.issue.ticketing.issue.enumerations.Status;
import com.olfa.issue.ticketing.issue.exceptions.IOError;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public class IssueRepositoryImpl implements IssueRepository {
    private final IssueRepositoryJPA issueRepositoryJPA;

    public IssueRepositoryImpl(IssueRepositoryJPA issueRepositoryJPA) {
        this.issueRepositoryJPA = issueRepositoryJPA;
    }

    @Override
    public Issue createIssue(Issue issue) {
        return issueRepositoryJPA.save(issue);
    }

    @Override
    public IssueResult updateIssue(Issue issue) {
        var retrievedIssue = this.issueRepositoryJPA
                .findById(issue.getId());
        if (retrievedIssue.isEmpty())
            return new IssueResult.Failure("", new IOError.IssueNotFoundError());

        return new IssueResult.Success(this.issueRepositoryJPA.save(issue));
    }

    @Override
    public IssueResult getIssue(UUID issueId) {
        var retrievedIssue = this.issueRepositoryJPA
                .findById(issueId);
        if (retrievedIssue.isEmpty())
            return new IssueResult.Failure("", new IOError.IssueNotFoundError());
        return new IssueResult.Success(retrievedIssue.get());
    }

    @Override
    public Collection<Issue> getAllIssues() {
        return this.issueRepositoryJPA
                .findAll();
    }

    @Override
    public Collection<Issue> getAllIssuesByStatus(Status status) {
        return this.issueRepositoryJPA
                .getIssuesByStatus(status);
    }

    @Override
    public Collection<Issue> getAllIssuesByRangeDate(LocalDate startDate, LocalDate endDate) {
        return this.issueRepositoryJPA
                .getIssuesBetweenDates(startDate, endDate);
    }

    @Override
    public Collection<Issue> getAllIssuesByPriority(int priority) {
        return this.issueRepositoryJPA
                .getIssuesByPriority(priority);
    }

    @Override
    public IssueResult deleteIssueById(UUID issueId) {
        var retrievedIssue = this.issueRepositoryJPA
                .findById(issueId);
        if (retrievedIssue.isEmpty())
            return new IssueResult.Failure("", new IOError.IssueNotFoundError());
        this.issueRepositoryJPA.deleteById(issueId);
        return new IssueResult.Success(retrievedIssue.get());
    }
}

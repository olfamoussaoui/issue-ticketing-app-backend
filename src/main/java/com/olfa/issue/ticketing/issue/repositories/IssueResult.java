package com.olfa.issue.ticketing.issue.repositories;

import com.olfa.issue.ticketing.issue.entities.Issue;

sealed interface IssueResult permits IssueResult.Success, IssueResult.Failure {
    record Success(Issue issue) implements IssueResult {}
    record Failure(String message, IssueRepository.IOError cause) implements IssueResult {}
}

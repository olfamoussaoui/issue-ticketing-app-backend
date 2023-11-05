package com.olfa.issue.ticketing.issue.repositories;

import com.olfa.issue.ticketing.issue.entities.Issue;
import com.olfa.issue.ticketing.issue.exceptions.IOError;

public sealed interface IssueResult permits IssueResult.Success, IssueResult.Failure {
    record Success(Issue issue) implements IssueResult {}
    record Failure(String message, IOError cause) implements IssueResult {}
}

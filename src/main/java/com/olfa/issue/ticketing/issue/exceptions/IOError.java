package com.olfa.issue.ticketing.issue.exceptions;

public sealed interface IOError permits IOError.IssueNotFoundError {
    record IssueNotFoundError() implements IOError {
    }
}

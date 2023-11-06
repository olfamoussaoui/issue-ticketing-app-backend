package com.olfa.issue.ticketing.issue.dtos;

import com.olfa.issue.ticketing.issue.exceptions.IOError;

public sealed interface IssueResultDto permits IssueResultDto.Failure, IssueResultDto.Success {
    record Success(IssueResponseDto issueResponseDto) implements IssueResultDto {
    }

    record Failure(String message, IOError cause) implements IssueResultDto {
    }
}

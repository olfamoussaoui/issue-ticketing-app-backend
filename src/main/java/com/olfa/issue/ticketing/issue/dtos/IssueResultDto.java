package com.olfa.issue.ticketing.issue.dtos;

import com.olfa.issue.ticketing.issue.exceptions.IOError;

sealed public interface IssueResultDto permits IssueResultDto.Failure, IssueResultDto.Success {
    record Success(IssueDto issueDto) implements IssueResultDto {
    }

    record Failure(String message, IOError cause) implements IssueResultDto {
    }
}

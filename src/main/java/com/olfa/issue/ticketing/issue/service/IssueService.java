package com.olfa.issue.ticketing.issue.service;

import com.olfa.issue.ticketing.issue.dtos.IssueResponseDto;
import com.olfa.issue.ticketing.issue.dtos.IssueResultDto;
import com.olfa.issue.ticketing.issue.entities.Issue;
import com.olfa.issue.ticketing.issue.enumerations.Status;
import com.olfa.issue.ticketing.issue.mapper.IssueResponseDTOMapper;
import com.olfa.issue.ticketing.issue.repositories.IssueRepository;
import com.olfa.issue.ticketing.issue.repositories.IssueResult;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class IssueService {
    private final IssueRepository issueRepository;
    private final IssueResponseDTOMapper issueResponseDTOMapper;

    public IssueService(IssueRepository issueRepository, IssueResponseDTOMapper issueResponseDTOMapper) {
        this.issueRepository = issueRepository;
        this.issueResponseDTOMapper = issueResponseDTOMapper;
    }

    public IssueResponseDto createIssue(Issue issueRequest) {
        var createdIssue = this.issueRepository
                .createIssue(issueRequest);
        return issueResponseDTOMapper.apply(createdIssue);
    }

    public IssueResultDto updateIssue(Issue issueRequest) {
        var updatedIssue = this.issueRepository
                .updateIssue(issueRequest);
        return switch (updatedIssue) {
            case IssueResult.Success s -> new IssueResultDto.Success(issueResponseDTOMapper.apply(s.issue()));
            case IssueResult.Failure f -> new IssueResultDto.Failure(f.message(), f.cause());
        };
    }

    public IssueResultDto getIssue(UUID issueId) {
        var retreivedIssue = this.issueRepository
                .getIssue(issueId);
        return switch (retreivedIssue) {
            case IssueResult.Success s -> new IssueResultDto.Success(issueResponseDTOMapper.apply(s.issue()));
            case IssueResult.Failure f -> new IssueResultDto.Failure(f.message(), f.cause());
        };
    }

    public Collection<IssueResponseDto> getAllIssues() {
        return this.issueRepository
                .getAllIssues()
                .stream()
                .map(issueResponseDTOMapper)
                .collect(Collectors.toList());
    }

    public Collection<IssueResponseDto> getAllIssuesByStatus(Status status) {
        return this.issueRepository
                .getAllIssuesByStatus(status)
                .stream()
                .map(issueResponseDTOMapper)
                .collect(Collectors.toList());
    }

    public Collection<IssueResponseDto> getAllIssuesByRangeDate(LocalDate startDate, LocalDate endDate) {
        return this.issueRepository
                .getAllIssuesByRangeDate(startDate, endDate)
                .stream()
                .map(issueResponseDTOMapper)
                .collect(Collectors.toList());
    }

    public Collection<IssueResponseDto> getAllIssuesByPriority(int priority) {
        return this.issueRepository
                .getAllIssuesByPriority(priority)
                .stream()
                .map(issueResponseDTOMapper)
                .collect(Collectors.toList());
    }

    public IssueResultDto deleteIssue(UUID issueId) {
        var deletedIssue = this.issueRepository
                .deleteIssueById(issueId);
        return switch (deletedIssue) {
            case IssueResult.Success s -> new IssueResultDto.Success(issueResponseDTOMapper.apply(s.issue()));
            case IssueResult.Failure f -> new IssueResultDto.Failure(f.message(), f.cause());
        };
    }
}

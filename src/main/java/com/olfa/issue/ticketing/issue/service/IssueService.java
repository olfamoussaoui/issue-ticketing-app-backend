package com.olfa.issue.ticketing.issue.service;

import com.olfa.issue.ticketing.issue.dtos.IssueRangeDateDto;
import com.olfa.issue.ticketing.issue.dtos.IssueRequestDto;
import com.olfa.issue.ticketing.issue.dtos.IssueResponseDto;
import com.olfa.issue.ticketing.issue.dtos.IssueResultDto;
import com.olfa.issue.ticketing.issue.enumerations.Status;
import com.olfa.issue.ticketing.issue.mapper.IssueResponseDTOMapper;
import com.olfa.issue.ticketing.issue.repositories.IssueRepository;
import com.olfa.issue.ticketing.issue.repositories.IssueResult;

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

    public IssueResponseDto createIssue(IssueRequestDto issueDto) {
        var createdIssue = this.issueRepository
                .createIssue(IssueRequestDto.toIssue(issueDto));
        return issueResponseDTOMapper.apply(createdIssue);
    }

    public IssueResultDto updateIssue(IssueRequestDto issueDto) {
        var updatedIssue = this.issueRepository
                .updateIssue(IssueRequestDto.toIssue(issueDto));
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

    public Collection<IssueResponseDto> getAllIssuesByRangeDate(IssueRangeDateDto issueRangeDateDto) {
        return this.issueRepository
                .getAllIssuesByRangeDate(
                        issueRangeDateDto.startDate(),
                        issueRangeDateDto.endDate())
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

package com.olfa.issue.ticketing.issue.service;

import com.olfa.issue.ticketing.issue.dtos.IssueDto;
import com.olfa.issue.ticketing.issue.dtos.IssueRangeDateDto;
import com.olfa.issue.ticketing.issue.dtos.IssueResultDto;
import com.olfa.issue.ticketing.issue.enumerations.Status;
import com.olfa.issue.ticketing.issue.mapper.IssueDTOMapper;
import com.olfa.issue.ticketing.issue.repositories.IssueRepository;
import com.olfa.issue.ticketing.issue.repositories.IssueResult;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class IssueService {
    private final IssueRepository issueRepository;
    private final IssueDTOMapper issueDTOMapper;

    public IssueService(IssueRepository issueRepository, IssueDTOMapper issueDTOMapper) {
        this.issueRepository = issueRepository;
        this.issueDTOMapper = issueDTOMapper;
    }

    public IssueDto createIssue(IssueDto issueDto) {
        var createdIssue = this.issueRepository
                .createIssue(IssueDto.toIssue(issueDto));
        return issueDTOMapper.apply(createdIssue);
    }

    public IssueResultDto updateIssue(IssueDto issueDto) {
        var updatedIssue = this.issueRepository
                .updateIssue(IssueDto.toIssue(issueDto));
        return switch (updatedIssue) {
            case IssueResult.Success s -> new IssueResultDto.Success(issueDTOMapper.apply(s.issue()));
            case IssueResult.Failure f -> new IssueResultDto.Failure(f.message(), f.cause());
        };
    }

    public IssueResultDto getIssue(UUID issueId) {
        var retreivedIssue = this.issueRepository
                .getIssue(issueId);
        return switch (retreivedIssue) {
            case IssueResult.Success s -> new IssueResultDto.Success(issueDTOMapper.apply(s.issue()));
            case IssueResult.Failure f -> new IssueResultDto.Failure(f.message(), f.cause());
        };
    }

    public Collection<IssueDto> getAllIssues() {
        return this.issueRepository
                .getAllIssues()
                .stream()
                .map(issueDTOMapper)
                .collect(Collectors.toList());
    }

    public Collection<IssueDto> getAllIssuesByStatus(Status status) {
        return this.issueRepository
                .getAllIssuesByStatus(status)
                .stream()
                .map(issueDTOMapper)
                .collect(Collectors.toList());
    }

    public Collection<IssueDto> getAllIssuesByRangeDate(IssueRangeDateDto issueRangeDateDto) {
        return this.issueRepository
                .getAllIssuesByRangeDate(
                        issueRangeDateDto.startDate(),
                        issueRangeDateDto.endDate())
                .stream()
                .map(issueDTOMapper)
                .collect(Collectors.toList());
    }

    public Collection<IssueDto> getAllIssuesByPriority(int priority) {
        return this.issueRepository
                .getAllIssuesByPriority(priority)
                .stream()
                .map(issueDTOMapper)
                .collect(Collectors.toList());
    }

    public IssueResultDto deleteIssue(UUID issueId) {
        var deletedIssue = this.issueRepository
                .deleteIssueById(issueId);
        return switch (deletedIssue) {
            case IssueResult.Success s -> new IssueResultDto.Success(issueDTOMapper.apply(s.issue()));
            case IssueResult.Failure f -> new IssueResultDto.Failure(f.message(), f.cause());
        };
    }
}

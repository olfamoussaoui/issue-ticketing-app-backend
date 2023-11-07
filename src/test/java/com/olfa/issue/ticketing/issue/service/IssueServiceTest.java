package com.olfa.issue.ticketing.issue.service;

import com.olfa.issue.ticketing.issue.dtos.IssueResponseDto;
import com.olfa.issue.ticketing.issue.dtos.IssueResultDto;
import com.olfa.issue.ticketing.issue.entities.Issue;
import com.olfa.issue.ticketing.issue.enumerations.Status;
import com.olfa.issue.ticketing.issue.exceptions.IOError;
import com.olfa.issue.ticketing.issue.mapper.IssueResponseDTOMapper;
import com.olfa.issue.ticketing.issue.repositories.IssueRepository;
import com.olfa.issue.ticketing.issue.repositories.IssueResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {
    @Mock
    private IssueRepository issueRepository;
    private final IssueResponseDTOMapper issueResponseDTOMapper = new IssueResponseDTOMapper();
    @InjectMocks
    private IssueService issueService;

    @BeforeEach
    void setUp() {
        this.issueService = new IssueService(issueRepository, issueResponseDTOMapper);
    }

    @Test
    void testCreateIssueReturnsIssueDto() {
        final Issue issueRequest = Issue.builder()
                .description("Some problem with the PC")
                .category("Hardware")
                .priority(3)
                .progress(0)
                .status(Status.OPEN)
                .active(true)
                .build();

        final Issue issue = Issue.builder()
                .id(UUID.randomUUID())
                .description("Some problem with the PC")
                .category("Hardware")
                .priority(3)
                .progress(0)
                .status(Status.OPEN)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(issueRepository.createIssue(Mockito.any(Issue.class)))
                .thenReturn(issue);

        var savedIssue = issueService.createIssue(issueRequest);

        assertAll(
                () -> assertNotNull(savedIssue),
                () -> assertSame(Status.OPEN, savedIssue.status()),
                () -> assertTrue(savedIssue.active())
        );
    }

    @Test
    void testUpdateIssueReturnsIssueDtoSuccess() {
        final Issue issueRequest = Issue.builder()
                .description("Some problem with the server")
                .category("Hardware")
                .priority(3)
                .progress(0)
                .status(Status.OPEN)
                .active(true)
                .build();
        final LocalDateTime issueUpdatedTime = LocalDateTime.now();
        final Issue issue = Issue.builder()
                .id(UUID.randomUUID())
                .description("Some problem with the server")
                .category("Hardware")
                .priority(3)
                .progress(0)
                .status(Status.IN_PROGRESS)
                .active(true)
                .createdAt(LocalDateTime.now().minusHours(4))
                .updatedAt(issueUpdatedTime)
                .build();

        when(issueRepository.updateIssue(Mockito.any(Issue.class)))
                .thenReturn(new IssueResult.Success(issue));

        var updatedIssue = (IssueResultDto.Success) issueService.updateIssue(issueRequest);
        assertAll(
                () -> assertNotNull(updatedIssue.issueResponseDto()),
                () -> assertSame(Status.IN_PROGRESS, updatedIssue.issueResponseDto().status()),
                () -> assertSame(issueUpdatedTime, updatedIssue.issueResponseDto().updatedTime())
        );
    }

    @Test
    void testUpdateIssueReturnsIssueDtoFailure() {
        final Issue issueRequest = Issue.builder()
                .id(UUID.randomUUID())
                .description("Some problem with the server")
                .category("Hardware")
                .priority(3)
                .progress(0)
                .status(Status.OPEN)
                .active(true)
                .build();

        when(issueRepository.updateIssue(Mockito.any(Issue.class)))
                .thenReturn(new IssueResult.Failure("", new IOError.IssueNotFoundError()));

        var updatedIssue = (IssueResultDto.Failure) issueService.updateIssue(issueRequest);
        assertAll(
                () -> assertNotNull(updatedIssue),
                () -> assertSame(IOError.IssueNotFoundError.class, updatedIssue.cause().getClass())
        );
    }

    @Test
    void testGetIssueReturnsIssueDtoSuccess() {
        final UUID issueId = UUID.randomUUID();
        final Issue issue = Issue.builder()
                .id(issueId)
                .description("Some problem with the server")
                .category("Hardware")
                .priority(3)
                .progress(0)
                .status(Status.IN_PROGRESS)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(issueRepository.getIssue(Mockito.any(UUID.class)))
                .thenReturn(new IssueResult.Success(issue));

        var retrievedIssue = (IssueResultDto.Success) issueService.getIssue(issueId);

        assertAll(
                () -> assertNotNull(retrievedIssue.issueResponseDto()),
                () -> assertSame(issue.getStatus(), retrievedIssue.issueResponseDto().status()),
                () -> assertSame(issue.getCreatedAt(), retrievedIssue.issueResponseDto().creationDate())
        );
    }

    @Test
    void testGetIssueReturnsIssueDtoFailure() {
        final UUID issueId = UUID.randomUUID();
        when(issueRepository.getIssue(Mockito.any(UUID.class)))
                .thenReturn(new IssueResult.Failure("", new IOError.IssueNotFoundError()));

        var retrievedIssue = (IssueResultDto.Failure) issueService.getIssue(issueId);

        assertAll(
                () -> assertNotNull(retrievedIssue),
                () -> assertSame(IOError.IssueNotFoundError.class, retrievedIssue.cause().getClass())
        );
    }

    @Test
    void testGetAllIssuesReturnsListOfIssuesResponseDto() {
        var issueList = List.of(
                Issue.builder()
                        .id(UUID.randomUUID())
                        .description("Some problem with the server")
                        .category("Hardware")
                        .priority(3)
                        .progress(0)
                        .status(Status.OPEN)
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),
                Issue.builder()
                        .id(UUID.randomUUID())
                        .description("Some problem with the Pc")
                        .category("Hardware")
                        .priority(3)
                        .progress(0)
                        .status(Status.IN_PROGRESS)
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),
                Issue.builder()
                        .id(UUID.randomUUID())
                        .description("I need another screen")
                        .category("Hardware")
                        .priority(3)
                        .progress(0)
                        .status(Status.CLOSED)
                        .active(false)
                        .createdAt(LocalDateTime.now().minusDays(6))
                        .updatedAt(LocalDateTime.now())
                        .build(),
                Issue.builder()
                        .id(UUID.randomUUID())
                        .description("Some problem with the server")
                        .category("Hardware")
                        .priority(3)
                        .progress(0)
                        .status(Status.OPEN)
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );
        when(issueRepository.getAllIssues())
                .thenReturn(issueList);

        var retreivedIssues = this.issueService.getAllIssues();
        assertAll(
                () -> assertFalse(retreivedIssues.isEmpty()),
                () -> assertEquals(4, retreivedIssues.size())
        );
    }

    @Test
    void testGetAllIssuesByStatusReturnsListOfIssuesResponseDto() {
        var issueList = List.of(
                Issue.builder()
                        .id(UUID.randomUUID())
                        .description("Some problem with the server")
                        .category("Hardware")
                        .priority(3)
                        .progress(0)
                        .status(Status.OPEN)
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),
                Issue.builder()
                        .id(UUID.randomUUID())
                        .description("Some problem with the PC")
                        .category("Hardware")
                        .priority(3)
                        .progress(0)
                        .status(Status.OPEN)
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );
        when(issueRepository.getAllIssuesByStatus(Mockito.any(Status.class)))
                .thenReturn(issueList);

        var retreivedIssues = this.issueService.getAllIssuesByStatus(Status.OPEN);
        var retreivedIssuesStatus = retreivedIssues.stream().map(IssueResponseDto::status).collect(Collectors.toSet());
        assertAll(
                () -> assertFalse(retreivedIssues.isEmpty()),
                () -> assertEquals(2, retreivedIssues.size()),
                () -> assertEquals(1, retreivedIssuesStatus.size())
        );
    }

    @Test
    void testGetAllIssuesByRangeDateReturnsListOfIssuesResponseDto() {
        var issueList = List.of(
                Issue.builder()
                        .id(UUID.randomUUID())
                        .description("Some problem with the server")
                        .category("Hardware")
                        .priority(3)
                        .progress(0)
                        .status(Status.OPEN)
                        .active(true)
                        .createdAt(LocalDateTime.now().minusDays(6))
                        .updatedAt(LocalDateTime.now())
                        .build(),
                Issue.builder()
                        .id(UUID.randomUUID())
                        .description("Some problem with the PC")
                        .category("Hardware")
                        .priority(3)
                        .progress(0)
                        .status(Status.OPEN)
                        .active(true)
                        .createdAt(LocalDateTime.now().minusDays(7))
                        .updatedAt(LocalDateTime.now())
                        .build(),
                Issue.builder()
                        .id(UUID.randomUUID())
                        .description("Need pay recap")
                        .category("Administration")
                        .priority(3)
                        .progress(0)
                        .status(Status.OPEN)
                        .active(true)
                        .createdAt(LocalDateTime.now().minusDays(1))
                        .updatedAt(LocalDateTime.now())
                        .build(),
                Issue.builder()
                        .id(UUID.randomUUID())
                        .description("Some problem with the PC")
                        .category("Hardware")
                        .priority(3)
                        .progress(0)
                        .status(Status.IN_PROGRESS)
                        .active(true)
                        .createdAt(LocalDateTime.now().minusDays(5))
                        .updatedAt(LocalDateTime.now())
                        .build()
        );
        var startDate = LocalDate.now().minusDays(10);
        var endDate = LocalDate.now();
        when(issueRepository
                .getAllIssuesByRangeDate(
                        startDate,
                        endDate)
        ).thenReturn(issueList);
        var retreivedIssues = this.issueService.getAllIssuesByRangeDate(startDate, endDate);
        verify(issueRepository).getAllIssuesByRangeDate(startDate, endDate);

        assertAll(
                () -> assertFalse(retreivedIssues.isEmpty()),
                () -> assertEquals(4, retreivedIssues.size())
        );
    }

    @Test
    void testGetAllIssuesByPriorityReturnsListOfIssuesResponseDto() {
        var issueList = List.of(
                Issue.builder()
                        .id(UUID.randomUUID())
                        .description("Some problem with the server")
                        .category("Hardware")
                        .priority(3)
                        .progress(0)
                        .status(Status.OPEN)
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),
                Issue.builder()
                        .id(UUID.randomUUID())
                        .description("Some problem with the PC")
                        .category("Hardware")
                        .priority(3)
                        .progress(0)
                        .status(Status.OPEN)
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );
        when(issueRepository.getAllIssuesByPriority(Mockito.anyInt()))
                .thenReturn(issueList);

        var retreivedIssues = this.issueService.getAllIssuesByPriority(3);
        var retreivedIssuesPriorities = retreivedIssues.stream().map(IssueResponseDto::priority).collect(Collectors.toSet());
        assertAll(
                () -> assertFalse(retreivedIssues.isEmpty()),
                () -> assertEquals(2, retreivedIssues.size()),
                () -> assertEquals(1, retreivedIssuesPriorities.size())
        );
    }

    @Test
    void testDeleteIssueReturnsIssueDtoSuccess() {
        final UUID issueId = UUID.randomUUID();
        final Issue issue = Issue.builder()
                .id(issueId)
                .description("Some problem with the server")
                .category("Hardware")
                .priority(3)
                .progress(0)
                .status(Status.IN_PROGRESS)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(issueRepository.deleteIssueById(Mockito.any(UUID.class)))
                .thenReturn(new IssueResult.Success(issue));

        var deletedIssue = (IssueResultDto.Success) issueService.deleteIssue(issueId);

        assertNotNull(deletedIssue.issueResponseDto());
    }

    @Test
    void testDeleteIssueReturnsIssueDtoFailure() {
        final UUID issueId = UUID.randomUUID();
        when(issueRepository.deleteIssueById(Mockito.any(UUID.class)))
                .thenReturn(new IssueResult.Failure("", new IOError.IssueNotFoundError()));

        var deletedIssue = (IssueResultDto.Failure) issueService.deleteIssue(issueId);

        assertAll(
                () -> assertNotNull(deletedIssue),
                () -> assertSame(IOError.IssueNotFoundError.class, deletedIssue.cause().getClass())
        );
    }
}
package com.olfa.issue.ticketing.issue.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.olfa.issue.ticketing.issue.dtos.IssueRequestDto;
import com.olfa.issue.ticketing.issue.dtos.IssueRequestRangeDateDto;
import com.olfa.issue.ticketing.issue.dtos.IssueResponseDto;
import com.olfa.issue.ticketing.issue.dtos.IssueResultDto;
import com.olfa.issue.ticketing.issue.entities.Issue;
import com.olfa.issue.ticketing.issue.enumerations.Category;
import com.olfa.issue.ticketing.issue.enumerations.Status;
import com.olfa.issue.ticketing.issue.exceptions.IOError;
import com.olfa.issue.ticketing.issue.mapper.IssueResponseDTOMapper;
import com.olfa.issue.ticketing.issue.service.IssueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.olfa.issue.ticketing.issue.dtos.validators.IssueRequestDtoValidator.ValidationResult.*;
import static com.olfa.issue.ticketing.issue.dtos.validators.IssueRequestRangeDateDtoValidator.ValidationResult.END_DATE_IS_BEFORE_START_DATE_ERROR;
import static com.olfa.issue.ticketing.issue.dtos.validators.IssueRequestRangeDateDtoValidator.ValidationResult.START_DATE_IN_FUTURE_ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest()
@AutoConfigureMockMvc
class IssueControllerTest {
    private final ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .build();
    private final IssueResponseDTOMapper issueResponseDTOMapper = new IssueResponseDTOMapper();
    @MockBean
    private IssueService issueService;
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private IssueController issueController;

    @BeforeEach
    void setUp() {
        this.issueController = new IssueController(issueService);
    }

    @Test
    void testCreateIssueReturnsCreatedStatus() throws Exception {
        final IssueRequestDto issueRequestDto = new IssueRequestDto(
                "Some problem with the PC",
                "Hardware",
                Category.HARDWARE,
                4,
                0,
                Status.OPEN,
                true);
        given(issueService.createIssue(any(Issue.class)))
                .willAnswer((invocation) -> {
                    Issue createdIssue = invocation.getArgument(0);
                    return issueResponseDTOMapper.apply(createdIssue);
                });

        mockMvc.perform(post("/api/v1/issues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(issueRequestDto)))
                .andExpect(status().isCreated());

        verify(issueService, times(1)).createIssue(any());
        verifyNoMoreInteractions(issueService);
    }

    @Test
    void testCreateIssueReturnsBadRequestStatus() throws Exception {
        final IssueRequestDto issueRequestDto = new IssueRequestDto(
                "",
                "",
                Category.HARDWARE,
                10,
                0,
                Status.OPEN,
                true);
        given(issueService.createIssue(any(Issue.class)))
                .willAnswer((invocation) -> {
                    Issue createdIssue = invocation.getArgument(0);
                    return issueResponseDTOMapper.apply(createdIssue);
                });

        mockMvc.perform(post("/api/v1/issues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(issueRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(header().string(
                        String.valueOf(DESCRIPTION_NOT_VALID),
                        String.valueOf(DESCRIPTION_NOT_VALID).toLowerCase()))
                .andExpect(header().string(
                        String.valueOf(PRIORITY_NOT_VALID),
                        String.valueOf(PRIORITY_NOT_VALID).toLowerCase()))
                .andExpect(header().string(
                        String.valueOf(TITLE_NOT_VALID),
                        String.valueOf(TITLE_NOT_VALID).toLowerCase()));
    }

    @Test
    void testUpdateIssueReturnsOkStatus() throws Exception {
        final var issueId = UUID.randomUUID();
        final IssueRequestDto issueRequestDto = new IssueRequestDto(
                "Some problem with the PC",
                "Hardware",
                Category.HARDWARE,
                4,
                0,
                Status.IN_PROGRESS,
                true);
        final var issueResponseDto = new IssueResponseDto(
                issueId,
                issueRequestDto.title(),
                issueRequestDto.description(),
                issueRequestDto.category(),
                issueRequestDto.priority(),
                issueRequestDto.progress(),
                issueRequestDto.status(),
                issueRequestDto.active(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(issueService.updateIssue(any(Issue.class)))
                .thenReturn(new IssueResultDto.Success(issueResponseDto));

        mockMvc.perform(put("/api/v1/issues/" + issueId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(issueRequestDto)))
                .andExpect(status().isOk());

        verify(issueService, times(1)).updateIssue(any());
        verifyNoMoreInteractions(issueService);
    }

    @Test
    void testUpdateIssueWithValidRequestAndNonexistentIssueIdReturnsBadRequestStatus() throws Exception {
        final IssueRequestDto issueRequestDto = new IssueRequestDto(
                "Some problem with the PC",
                "Hardware",
                Category.HARDWARE,
                4,
                0,
                Status.IN_PROGRESS,
                true);

        when(issueService.updateIssue(any(Issue.class)))
                .thenReturn(new IssueResultDto.Failure("", new IOError.IssueNotFoundError()));

        mockMvc.perform(put("/api/v1/issues/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(issueRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(header().string(
                        IOError.IssueNotFoundError.class.getSimpleName(),
                        IOError.IssueNotFoundError.class.getSimpleName().toLowerCase()));

        verify(issueService, times(1)).updateIssue(any());
    }

    @Test
    void testUpdateIssueWithInValidRequestReturnsBadRequestStatus() throws Exception {
        final IssueRequestDto issueRequestDto = new IssueRequestDto(
                "",
                "",
                Category.HARDWARE,
                4,
                0,
                Status.IN_PROGRESS,
                true);

        mockMvc.perform(put("/api/v1/issues/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(issueRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(header().string(
                        DESCRIPTION_NOT_VALID.name(),
                        DESCRIPTION_NOT_VALID.name().toLowerCase()))
                .andExpect(header().string(
                        TITLE_NOT_VALID.name(),
                        TITLE_NOT_VALID.name().toLowerCase()));
    }

    @Test
    void testGetIssueWithExistentIssueIdReturnsOkStatus() throws Exception {
        final var issueId = UUID.randomUUID();
        final var issueResponseDto = new IssueResponseDto(
                issueId,
                "Some problem with the PC",
                "Hardware",
                Category.HARDWARE,
                4,
                0,
                Status.IN_PROGRESS,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(issueService.getIssue(any(UUID.class)))
                .thenReturn(new IssueResultDto.Success(issueResponseDto));

        mockMvc.perform(get("/api/v1/issues/" + issueId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(issueService, times(1)).getIssue(any());
        verifyNoMoreInteractions(issueService);
    }

    @Test
    void testGetIssueWithNonExistentIssueIdReturnsBadRequestStatus() throws Exception {
        final var issueId = UUID.randomUUID();
        when(issueService.getIssue(issueId))
                .thenReturn(new IssueResultDto.Failure("", new IOError.IssueNotFoundError()));

        mockMvc.perform(get("/api/v1/issues/" + issueId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(issueService, times(1)).getIssue(any());
        verifyNoMoreInteractions(issueService);
    }

    @Test
    void testGetAllIssuesReturnsOkStatus() throws Exception {
        final var issueResponseDtoList = List.of(
                new IssueResponseDto(UUID.randomUUID(),
                        "Some problem with the PC",
                        "Hardware",
                        Category.HARDWARE,
                        4,
                        0,
                        Status.IN_PROGRESS,
                        true,
                        LocalDateTime.now(),
                        LocalDateTime.now()),
                new IssueResponseDto(UUID.randomUUID(),
                        "Some problem with the Server",
                        "Hardware",
                        Category.HARDWARE,
                        5,
                        0,
                        Status.IN_PROGRESS,
                        true,
                        LocalDateTime.now(),
                        LocalDateTime.now())
        );
        when(issueService.getAllIssues())
                .thenReturn(issueResponseDtoList);

        mockMvc.perform(get("/api/v1/issues")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(issueService, times(1)).getAllIssues();
        verifyNoMoreInteractions(issueService);
    }

    @Test
    void testGetAllIssuesByStatusReturnsOkStatus() throws Exception {
        final var issueResponseDtoList = List.of(
                new IssueResponseDto(UUID.randomUUID(),
                        "Some problem with the PC",
                        "Hardware",
                        Category.HARDWARE,
                        4,
                        0,
                        Status.IN_PROGRESS,
                        true,
                        LocalDateTime.now(),
                        LocalDateTime.now()),
                new IssueResponseDto(UUID.randomUUID(),
                        "Some problem with the Server",
                        "Hardware",
                        Category.HARDWARE,
                        5,
                        0,
                        Status.IN_PROGRESS,
                        true,
                        LocalDateTime.now(),
                        LocalDateTime.now())
        );
        when(issueService.getAllIssuesByStatus(any(Status.class)))
                .thenReturn(issueResponseDtoList);

        mockMvc.perform(get("/api/v1/issues/status/IN_PROGRESS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(issueService, times(1)).getAllIssuesByStatus(any(Status.class));
        verifyNoMoreInteractions(issueService);
    }

    @Test
    void testGetAllIssuesByRangeDateWithValidDateReturnsOkStatus() throws Exception {
        var issueResponseDtoList = List.of(
                new IssueResponseDto(
                        UUID.randomUUID(),
                        "Some problem with the server",
                        "Hardware",
                        Category.HARDWARE,
                        3,
                        0,
                        Status.OPEN,
                        true,
                        LocalDateTime.now().minusDays(6),
                        LocalDateTime.now()),
                new IssueResponseDto(
                        UUID.randomUUID(),
                        "Some problem with the PC",
                        "Hardware",
                        Category.HARDWARE,
                        3,
                        0,
                        Status.OPEN,
                        true,
                        LocalDateTime.now().minusDays(7),
                        LocalDateTime.now()),
                new IssueResponseDto(
                        UUID.randomUUID(),
                        "Need pay recap",
                        "Administration",
                        Category.OTHER,
                        3,
                        0,
                        Status.OPEN,
                        true,
                        LocalDateTime.now().minusDays(1),
                        LocalDateTime.now()),
                new IssueResponseDto(
                        UUID.randomUUID(),
                        "Some problem with the PC",
                        "Hardware",
                        Category.HARDWARE,
                        3,
                        0,
                        Status.IN_PROGRESS,
                        true,
                        LocalDateTime.now().minusDays(5),
                        LocalDateTime.now()
                )
        );
        var startDate = LocalDate.now().minusDays(10);
        var endDate = LocalDate.now();
        when(issueService.getAllIssuesByRangeDate(startDate, endDate))
                .thenReturn(issueResponseDtoList);

        mockMvc.perform(post("/api/v1/issues/range")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new IssueRequestRangeDateDto(startDate, endDate)))
                )
                .andExpect(status().isOk());

        verify(issueService, times(1)).getAllIssuesByRangeDate(startDate, endDate);
        verifyNoMoreInteractions(issueService);
    }

    @Test
    void testGetAllIssuesByRangeDateWithInvalidDateReturnsBadRequestStatus() throws Exception {
        var startDate = LocalDate.now().plusDays(10);
        var endDate = LocalDate.now();
        mockMvc.perform(post("/api/v1/issues/range")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new IssueRequestRangeDateDto(startDate, endDate)))
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string(
                        String.valueOf(START_DATE_IN_FUTURE_ERROR),
                        String.valueOf(START_DATE_IN_FUTURE_ERROR).toLowerCase()))
                .andExpect(header().string(
                        String.valueOf(END_DATE_IS_BEFORE_START_DATE_ERROR),
                        String.valueOf(END_DATE_IS_BEFORE_START_DATE_ERROR).toLowerCase()));
    }

    @Test
    void testGetAllIssuesByPriorityReturnsOkStatus() throws Exception {
        var issueResponseDtoList = List.of(
                new IssueResponseDto(
                        UUID.randomUUID(),
                        "Some problem with the server",
                        "Hardware",
                        Category.HARDWARE,
                        3,
                        0,
                        Status.OPEN,
                        true,
                        LocalDateTime.now().minusDays(6),
                        LocalDateTime.now()),
                new IssueResponseDto(
                        UUID.randomUUID(),
                        "Some problem with the PC",
                        "Hardware",
                        Category.HARDWARE,
                        3,
                        0,
                        Status.OPEN,
                        true,
                        LocalDateTime.now().minusDays(7),
                        LocalDateTime.now()),
                new IssueResponseDto(
                        UUID.randomUUID(),
                        "Need pay recap",
                        "Administration",
                        Category.OTHER,
                        3,
                        0,
                        Status.OPEN,
                        true,
                        LocalDateTime.now().minusDays(1),
                        LocalDateTime.now()),
                new IssueResponseDto(
                        UUID.randomUUID(),
                        "Some problem with the PC",
                        "Hardware",
                        Category.HARDWARE,
                        3,
                        0,
                        Status.IN_PROGRESS,
                        true,
                        LocalDateTime.now().minusDays(5),
                        LocalDateTime.now()
                )
        );
        when(issueService.getAllIssuesByPriority(3))
                .thenReturn(issueResponseDtoList);

        mockMvc.perform(get("/api/v1/issues/priority/3")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(issueService, times(1)).getAllIssuesByPriority(3);
        verifyNoMoreInteractions(issueService);
    }

    @Test
    void testDeleteIssueWithExistentIssueIdReturnsOkStatus() throws Exception {
        final var issueId = UUID.randomUUID();
        final var issueResponseDto = new IssueResponseDto(
                issueId,
                "Some problem with the PC",
                "Hardware",
                Category.HARDWARE,
                4,
                0,
                Status.IN_PROGRESS,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(issueService.deleteIssue(any(UUID.class)))
                .thenReturn(new IssueResultDto.Success(issueResponseDto));

        mockMvc.perform(delete("/api/v1/issues/" + issueId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(issueService, times(1)).deleteIssue(any());
        verifyNoMoreInteractions(issueService);
    }

    @Test
    void testDeleteIssueWithNonExistentIssueIdReturnsBadRequestStatus() throws Exception {
        final var issueId = UUID.randomUUID();
        when(issueService.deleteIssue(issueId))
                .thenReturn(new IssueResultDto.Failure("", new IOError.IssueNotFoundError()));

        mockMvc.perform(delete("/api/v1/issues/" + issueId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(issueService, times(1)).deleteIssue(any());
        verifyNoMoreInteractions(issueService);
    }
}
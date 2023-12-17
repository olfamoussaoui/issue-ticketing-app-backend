package com.olfa.issue.ticketing.issue.rest;

import com.olfa.issue.ticketing.issue.dtos.IssueRequestDto;
import com.olfa.issue.ticketing.issue.dtos.IssueRequestRangeDateDto;
import com.olfa.issue.ticketing.issue.dtos.IssueResponseDto;
import com.olfa.issue.ticketing.issue.dtos.IssueResultDto;
import com.olfa.issue.ticketing.issue.dtos.validators.IssueRequestDtoValidator;
import com.olfa.issue.ticketing.issue.dtos.validators.IssueRequestRangeDateDtoValidator;
import com.olfa.issue.ticketing.issue.enumerations.Status;
import com.olfa.issue.ticketing.issue.service.IssueService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/issues")
@CrossOrigin(origins = "http://localhost:4200")
public class IssueController {
    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @PostMapping
    public ResponseEntity<IssueResponseDto> createIssue(@RequestBody IssueRequestDto issueRequestDto) {
        var errors = IssueRequestDtoValidator
                .isDescriptionValid()
                .and(IssueRequestDtoValidator.isTitleValid())
                .and(IssueRequestDtoValidator.isPriorityValid())
                .apply(issueRequestDto);
        var headers = new HttpHeaders();
        if (!errors.isEmpty()) {
            errors.forEach(err ->
                    headers.set(err.name(), err.name().toLowerCase())
            );
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .headers(headers)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.issueService
                        .createIssue(IssueRequestDto.toIssue(issueRequestDto)));
    }

    @PutMapping("/{issueId}")
    public ResponseEntity<IssueResponseDto> updateIssue(@PathVariable UUID issueId, @RequestBody IssueRequestDto issueRequestDto) {
        var errors = IssueRequestDtoValidator
                .isDescriptionValid()
                .and(IssueRequestDtoValidator.isTitleValid())
                .and(IssueRequestDtoValidator.isPriorityValid())
                .apply(issueRequestDto);
        var headers = new HttpHeaders();
        if (!errors.isEmpty()) {
            errors.forEach(err ->
                    headers.set(err.name(), err.name().toLowerCase())
            );
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .headers(headers)
                    .build();
        }

        var issueRequest = IssueRequestDto.toIssue(issueRequestDto);
        issueRequest.setId(issueId);
        var updatedIssue = this.issueService
                .updateIssue(issueRequest);
        return switch (updatedIssue) {
            case IssueResultDto.Success s -> ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(s.issueResponseDto());
            case IssueResultDto.Failure f -> {
                headers.set(f.cause().getClass().getSimpleName(),
                        f.cause().getClass().getSimpleName().toLowerCase());
                yield ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .headers(headers)
                        .build();
            }
        };
    }


    @GetMapping("/{issueId}")
    public ResponseEntity<IssueResponseDto> getIssue(@PathVariable UUID issueId) {
        var retreivedIssue = this.issueService
                .getIssue(issueId);
        var headers = new HttpHeaders();
        return switch (retreivedIssue) {
            case IssueResultDto.Success s -> ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(s.issueResponseDto());
            case IssueResultDto.Failure f -> {
                headers.set(f.cause().getClass().getSimpleName(),
                        f.cause().getClass().getSimpleName().toLowerCase());
                yield ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .headers(headers)
                        .build();
            }
        };
    }

    @GetMapping("")
    public ResponseEntity<Collection<IssueResponseDto>> getAllIssues() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.issueService
                        .getAllIssues());
    }

    @GetMapping("/status/{issueStatus}")
    public ResponseEntity<Collection<IssueResponseDto>> getAllIssuesByStatus(@PathVariable Status issueStatus) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.issueService
                        .getAllIssuesByStatus(issueStatus));
    }


    @PostMapping("/range")
    public ResponseEntity<Collection<IssueResponseDto>> getAllIssuesByRangeDate(@RequestBody IssueRequestRangeDateDto issueRequestRangeDateDto) {
        var errors = IssueRequestRangeDateDtoValidator
                .checkStartDate()
                .and(IssueRequestRangeDateDtoValidator.checkEndDateIsAfterStartDate())
                .apply(issueRequestRangeDateDto);
        var headers = new HttpHeaders();
        if (!errors.isEmpty()) {
            errors.forEach(err ->
                    headers.set(err.name(), err.name().toLowerCase())
            );
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .headers(headers)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.issueService
                        .getAllIssuesByRangeDate(issueRequestRangeDateDto.startDate(), issueRequestRangeDateDto.endDate()));
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<Collection<IssueResponseDto>> getAllIssuesByPriority(@PathVariable int priority) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.issueService
                        .getAllIssuesByPriority(priority));
    }

    @DeleteMapping("/{issueId}")
    public ResponseEntity<IssueResponseDto> deleteIssue(@PathVariable UUID issueId) {
        var deletedIssue = this.issueService
                .deleteIssue(issueId);
        var headers = new HttpHeaders();
        return switch (deletedIssue) {
            case IssueResultDto.Success s -> ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(s.issueResponseDto());
            case IssueResultDto.Failure f -> {
                headers.set(f.cause().getClass().getSimpleName(),
                        f.cause().getClass().getSimpleName().toLowerCase());
                yield ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .headers(headers)
                        .build();
            }
        };
    }
}

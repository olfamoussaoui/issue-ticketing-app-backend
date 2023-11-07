package com.olfa.issue.ticketing.issue.dtos.validators;

import com.olfa.issue.ticketing.issue.dtos.IssueRequestDto;
import com.olfa.issue.ticketing.issue.enumerations.Status;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.olfa.issue.ticketing.issue.dtos.validators.IssueRequestDtoValidator.ValidationResult.DESCRIPTION_NOT_VALID;
import static com.olfa.issue.ticketing.issue.dtos.validators.IssueRequestDtoValidator.ValidationResult.PRIORITY_NOT_VALID;
import static org.junit.jupiter.api.Assertions.*;

class IssueRequestDtoValidatorTest {
    @Test
    void testValidateIssueRequestDtoWithEmptyDescriptionReturnsDESCRIPTION_NOT_VALID() {
        var issueRequestDto = new IssueRequestDto(
                "",
                "Hardware",
                3,
                0,
                Status.IN_PROGRESS,
                true
        );
        var errors = IssueRequestDtoValidator.isDescriptionValid()
                .and(IssueRequestDtoValidator.isPriorityValid())
                .apply(issueRequestDto);

        assertAll(
                () -> assertEquals(1, errors.size()),
                () -> assertTrue(errors.contains(DESCRIPTION_NOT_VALID))
        );
    }

    @Test
    void testValidateIssueRequestDtoWithNullDescriptionReturnsDESCRIPTION_NOT_VALID() {
        var issueRequestDto = new IssueRequestDto(
                null,
                "Hardware",
                3,
                0,
                Status.IN_PROGRESS,
                true
        );
        var errors = IssueRequestDtoValidator.isDescriptionValid()
                .and(IssueRequestDtoValidator.isPriorityValid())
                .apply(issueRequestDto);

        assertAll(
                () -> assertEquals(1, errors.size()),
                () -> assertTrue(errors.contains(DESCRIPTION_NOT_VALID))
        );
    }

    @Test
    void testValidateIssueRequestDtoWithNegativePriorityReturnsDESCRIPTION_NOT_VALID() {
        var issueRequestDto = new IssueRequestDto(
                "Some problem with the server",
                "Hardware",
                -1,
                0,
                Status.IN_PROGRESS,
                true
        );
        var errors = IssueRequestDtoValidator.isDescriptionValid()
                .and(IssueRequestDtoValidator.isPriorityValid())
                .apply(issueRequestDto);

        assertAll(
                () -> assertEquals(1, errors.size()),
                () -> assertTrue(errors.contains(PRIORITY_NOT_VALID))
        );
    }

    @Test
    void testValidateIssueRequestDtoWithPriorityOutOfFiveReturnsDESCRIPTION_NOT_VALID() {
        var issueRequestDto = new IssueRequestDto(
                "Some problem with the server",
                "Hardware",
                12,
                0,
                Status.IN_PROGRESS,
                true
        );
        var errors = IssueRequestDtoValidator.isDescriptionValid()
                .and(IssueRequestDtoValidator.isPriorityValid())
                .apply(issueRequestDto);

        assertAll(
                () -> assertEquals(1, errors.size()),
                () -> assertTrue(errors.contains(PRIORITY_NOT_VALID))
        );
    }

    @Test
    void testValidateIssueRequestDtoInvalidReturnsEnumSetWithTwoErrors() {
        var issueRequestDto = new IssueRequestDto(
                "",
                "Hardware",
                12,
                0,
                Status.IN_PROGRESS,
                true
        );
        var errors = IssueRequestDtoValidator.isDescriptionValid()
                .and(IssueRequestDtoValidator.isPriorityValid())
                .apply(issueRequestDto);

        assertAll(
                () -> assertEquals(2, errors.size()),
                () -> assertTrue(errors.containsAll(List.of(PRIORITY_NOT_VALID, DESCRIPTION_NOT_VALID)))
        );
    }

    @Test
    void testValidateIssueRequestDtoReturnsEmptySet() {
        var issueRequestDto = new IssueRequestDto(
                "Some problem with the server",
                "Hardware",
                5,
                0,
                Status.IN_PROGRESS,
                true
        );
        var errors = IssueRequestDtoValidator.isDescriptionValid()
                .and(IssueRequestDtoValidator.isPriorityValid())
                .apply(issueRequestDto);

        assertTrue(errors.isEmpty());
    }
}
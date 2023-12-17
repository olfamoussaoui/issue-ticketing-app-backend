package com.olfa.issue.ticketing.issue.dtos.validators;

import com.olfa.issue.ticketing.issue.dtos.IssueRequestDto;
import com.olfa.issue.ticketing.issue.enumerations.Category;
import com.olfa.issue.ticketing.issue.enumerations.Status;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.olfa.issue.ticketing.issue.dtos.validators.IssueRequestDtoValidator.ValidationResult.*;
import static org.junit.jupiter.api.Assertions.*;

class IssueRequestDtoValidatorTest {
    @Test
    void testValidateIssueRequestDtoWithEmptyDescriptionReturnsDESCRIPTION_NOT_VALID() {
        var issueRequestDto = new IssueRequestDto(
                "Ticket Title",
                "",
                Category.HARDWARE,
                3,
                0,
                Status.IN_PROGRESS,
                true
        );
        var errors = IssueRequestDtoValidator.isDescriptionValid()
                .and(IssueRequestDtoValidator.isTitleValid())
                .and(IssueRequestDtoValidator.isPriorityValid())
                .apply(issueRequestDto);

        assertAll(
                () -> assertEquals(1, errors.size()),
                () -> assertTrue(errors.contains(DESCRIPTION_NOT_VALID))
        );
    }
    @Test
    void testValidateIssueRequestDtoWithEmptyTitleReturnsTitle_NOT_VALID() {
        var issueRequestDto = new IssueRequestDto(
                "",
                "Hardware",
                Category.HARDWARE,
                3,
                0,
                Status.IN_PROGRESS,
                true
        );
        var errors = IssueRequestDtoValidator.isDescriptionValid()
                .and(IssueRequestDtoValidator.isTitleValid())
                .and(IssueRequestDtoValidator.isPriorityValid())
                .apply(issueRequestDto);

        assertAll(
                () -> assertEquals(1, errors.size()),
                () -> assertTrue(errors.contains(TITLE_NOT_VALID))
        );
    }

    @Test
    void testValidateIssueRequestDtoWithNullDescriptionReturnsDESCRIPTION_NOT_VALID() {
        var issueRequestDto = new IssueRequestDto(
                "Title of ticket",
                null,
                Category.HARDWARE,
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
    void testValidateIssueRequestDtoWithNullTitleReturnsTITLE_NOT_VALID() {
        var issueRequestDto = new IssueRequestDto(
                null,
                "Hardware",
                Category.HARDWARE,
                3,
                0,
                Status.IN_PROGRESS,
                true
        );
        var errors = IssueRequestDtoValidator.isTitleValid()
                .and(IssueRequestDtoValidator.isDescriptionValid())
                .and(IssueRequestDtoValidator.isPriorityValid())
                .apply(issueRequestDto);

        assertAll(
                () -> assertEquals(1, errors.size()),
                () -> assertTrue(errors.contains(TITLE_NOT_VALID))
        );
    }

    @Test
    void testValidateIssueRequestDtoWithNegativePriorityReturnsDESCRIPTION_NOT_VALID() {
        var issueRequestDto = new IssueRequestDto(
                "Some problem with the server",
                "Hardware",
                Category.HARDWARE,
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
                Category.HARDWARE,
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
                Category.HARDWARE,
                12,
                0,
                Status.IN_PROGRESS,
                true
        );
        var errors = IssueRequestDtoValidator.isTitleValid()
                .and(IssueRequestDtoValidator.isDescriptionValid())
                .and(IssueRequestDtoValidator.isPriorityValid())
                .apply(issueRequestDto);

        assertAll(
                () -> assertEquals(2, errors.size()),
                () -> assertTrue(errors.containsAll(List.of(PRIORITY_NOT_VALID, TITLE_NOT_VALID)))
        );
    }

    @Test
    void testValidateIssueRequestDtoReturnsEmptySet() {
        var issueRequestDto = new IssueRequestDto(
                "Some problem with the server",
                "Hardware",
                Category.HARDWARE,
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
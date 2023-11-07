package com.olfa.issue.ticketing.issue.dtos.validators;

import com.olfa.issue.ticketing.issue.dtos.IssueRequestRangeDateDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.olfa.issue.ticketing.issue.dtos.validators.IssueRequestRangeDateDtoValidator.ValidationResult.END_DATE_IS_BEFORE_START_DATE_ERROR;
import static com.olfa.issue.ticketing.issue.dtos.validators.IssueRequestRangeDateDtoValidator.ValidationResult.START_DATE_IN_FUTURE_ERROR;
import static org.junit.jupiter.api.Assertions.*;

class IssueRequestRangeDateDtoValidatorTest {
    @Test
    void testIssueRequestRangeDateDtoValidatorWithStartDateInvalidReturnsSTART_DATE_IN_FUTURE_ERROR() {
        var issueRequestRangeDateDto = new IssueRequestRangeDateDto(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2)
        );
        var errors = IssueRequestRangeDateDtoValidator
                .checkStartDate()
                .and(IssueRequestRangeDateDtoValidator.checkEndDateIsAfterStartDate())
                .apply(issueRequestRangeDateDto);

        assertAll(
                () -> assertEquals(1, errors.size()),
                () -> assertTrue(errors.contains(START_DATE_IN_FUTURE_ERROR))
        );
    }
    @Test
    void testIssueRequestRangeDateDtoValidatorWithEndDateIsBeforeStartDateReturnsEND_DATE_IS_BEFORE_START_DATE_ERROR() {
        var issueRequestRangeDateDto = new IssueRequestRangeDateDto(
                LocalDate.now(),
                LocalDate.now().minusDays(4)
        );
        var errors = IssueRequestRangeDateDtoValidator
                .checkStartDate()
                .and(IssueRequestRangeDateDtoValidator.checkEndDateIsAfterStartDate())
                .apply(issueRequestRangeDateDto);

        assertAll(
                () -> assertEquals(1, errors.size()),
                () -> assertTrue(errors.contains(END_DATE_IS_BEFORE_START_DATE_ERROR))
        );
    }
    @Test
    void testIssueRequestRangeDateDtoValidatorWithValidIssueRequestRangeDateDtoReturnsEmptySet() {
        var issueRequestRangeDateDto = new IssueRequestRangeDateDto(
                LocalDate.now(),
                LocalDate.now().plusDays(4)
        );
        var errors = IssueRequestRangeDateDtoValidator
                .checkStartDate()
                .and(IssueRequestRangeDateDtoValidator.checkEndDateIsAfterStartDate())
                .apply(issueRequestRangeDateDto);

        assertTrue(errors.isEmpty());
    }
}
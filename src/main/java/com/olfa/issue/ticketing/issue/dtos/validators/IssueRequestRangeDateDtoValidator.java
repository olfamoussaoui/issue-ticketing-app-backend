package com.olfa.issue.ticketing.issue.dtos.validators;

import com.olfa.issue.ticketing.issue.dtos.IssueRequestRangeDateDto;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.function.Function;

public interface IssueRequestRangeDateDtoValidator
        extends Function<IssueRequestRangeDateDto, EnumSet<IssueRequestRangeDateDtoValidator.ValidationResult>> {
    static IssueRequestRangeDateDtoValidator checkStartDate() {
        return issueRequestRangeDateDto ->
                (!issueRequestRangeDateDto.startDate().isAfter(LocalDate.now()))
                        ? EnumSet.noneOf(IssueRequestRangeDateDtoValidator.ValidationResult.class)
                        : EnumSet.of(ValidationResult.START_DATE_IN_FUTURE_ERROR);
    }

    static IssueRequestRangeDateDtoValidator checkEndDateIsAfterStartDate() {
        return issueRequestRangeDateDto ->
                (issueRequestRangeDateDto.endDate().isAfter(issueRequestRangeDateDto.startDate()))
                        ? EnumSet.noneOf(IssueRequestRangeDateDtoValidator.ValidationResult.class)
                        : EnumSet.of(ValidationResult.END_DATE_IS_BEFORE_START_DATE_ERROR);
    }

    default IssueRequestRangeDateDtoValidator and(IssueRequestRangeDateDtoValidator other) {
        return issueRequestRangeDateDto -> {
            var combinedResult = EnumSet.copyOf(
                    this.apply(issueRequestRangeDateDto)
            );
            combinedResult.addAll(other.apply(issueRequestRangeDateDto));
            return combinedResult;
        };
    }

    enum ValidationResult {
        START_DATE_IN_FUTURE_ERROR,
        END_DATE_IS_BEFORE_START_DATE_ERROR
    }
}

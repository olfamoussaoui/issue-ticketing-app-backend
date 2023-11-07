package com.olfa.issue.ticketing.issue.dtos.validators;

import com.olfa.issue.ticketing.issue.dtos.IssueRequestDto;

import java.util.EnumSet;
import java.util.function.Function;

public interface IssueRequestDtoValidator
        extends Function<IssueRequestDto, EnumSet<IssueRequestDtoValidator.ValidationResult>> {

    static IssueRequestDtoValidator isDescriptionValid() {
        return issueRequestDto ->
                (issueRequestDto.description() != null && !issueRequestDto.description().isBlank())
                        ? EnumSet.noneOf(ValidationResult.class)
                        : EnumSet.of(ValidationResult.DESCRIPTION_NOT_VALID);
    }

    static IssueRequestDtoValidator isPriorityValid() {
        return issueRequestDto ->
                (issueRequestDto.priority() >= 0 && issueRequestDto.priority() <= 5)
                        ? EnumSet.noneOf(ValidationResult.class)
                        : EnumSet.of(ValidationResult.PRIORITY_NOT_VALID);
    }

    default IssueRequestDtoValidator and(IssueRequestDtoValidator other) {
        return issueRequestDto -> {
            var combinedResult = EnumSet.copyOf(
                    this.apply(issueRequestDto)
            );
            combinedResult.addAll(other.apply(issueRequestDto));
            return combinedResult;
        };
    }

    enum ValidationResult {
        DESCRIPTION_NOT_VALID,
        PRIORITY_NOT_VALID
    }
}

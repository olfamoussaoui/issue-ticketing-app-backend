package com.olfa.issue.ticketing.issue.dtos;

import java.time.LocalDate;

public record IssueRangeDateDto(
        LocalDate startDate,
        LocalDate endDate
) {
}

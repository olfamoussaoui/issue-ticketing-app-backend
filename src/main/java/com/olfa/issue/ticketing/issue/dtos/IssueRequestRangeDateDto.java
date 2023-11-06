package com.olfa.issue.ticketing.issue.dtos;

import java.time.LocalDate;

public record IssueRequestRangeDateDto(
        LocalDate startDate,
        LocalDate endDate
) {
}

package com.olfa.issue.ticketing.issue.repositories;

import com.olfa.issue.ticketing.issue.entities.Issue;
import com.olfa.issue.ticketing.issue.enumerations.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public interface IssueRepositoryJPA extends JpaRepository<Issue, UUID> {
    Collection<Issue> getIssuesByStatus(Status status);

    @Query("SELECT i FROM issues i WHERE i.createdAt >= :startDate AND i.createdAt <= :endDate")
    Collection<Issue> getIssuesBetweenDates(LocalDate startDate, LocalDate endDate);
    Collection<Issue> getIssuesByPriority(int priority);
}

package com.olfa.issue.ticketing.issue.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "issues")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class IssueDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String category;
    private Priority priority;
    private int progress;
    private Status status = Status.OPEN;
    private boolean active;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    enum Status {
        OPEN,
        IN_PROGRESS,
        CLOSED
    }

    enum Priority {
        ONE,
        TWO,
        THREE,
        FOR,
        FIVE
    }
}

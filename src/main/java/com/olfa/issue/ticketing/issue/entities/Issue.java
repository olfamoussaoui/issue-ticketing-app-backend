package com.olfa.issue.ticketing.issue.entities;

import com.olfa.issue.ticketing.issue.enumerations.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "issues")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String description;
    private String category;
    private int priority;
    private int progress;
    private Status status = Status.OPEN;
    private boolean active;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}

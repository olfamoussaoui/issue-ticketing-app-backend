package com.olfa.issue.ticketing.issue.entities;

import com.olfa.issue.ticketing.issue.enumerations.Category;
import com.olfa.issue.ticketing.issue.enumerations.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "issues")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String description;
    private Category category;
    private int priority;
    private int progress;
    @Builder.Default
    private Status status = Status.OPEN;
    private boolean active = true;
    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}

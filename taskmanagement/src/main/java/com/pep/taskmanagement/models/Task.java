package com.pep.taskmanagement.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.pep.taskmanagement.models.Enums.TaskPriority;
import com.pep.taskmanagement.models.Enums.TaskStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 5, message = "Title must be at least 5 characters long")
    private String title;

    @Size(max = 500, message = "Description can't exceed 500 characters")
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.TO_DO;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority = TaskPriority.MEDIUM;

    @FutureOrPresent(message = "Due date cannot be in the past")
    private LocalDate dueDate;

    private LocalDateTime createdAt;

    //foreign keys 
    private Long projectId;
    private Long userId;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    
}
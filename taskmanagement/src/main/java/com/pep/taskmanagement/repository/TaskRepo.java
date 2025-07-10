package com.pep.taskmanagement.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pep.taskmanagement.models.Task;
import com.pep.taskmanagement.models.Enums.TaskStatus;

public interface TaskRepo extends JpaRepository<Task, Long> {

    Page<Task> findByUserId(long userId, Pageable pageable);

    Page<Task> findByProjectId(long projectId, Pageable pageable);

    Page<Task> findByUserIdAndStatus(long userId, TaskStatus  status, Pageable pageable);

    Page<Task> findByProjectIdAndStatus(long projectId, TaskStatus  status, Pageable pageable);

    List<Task> findByDueDate(LocalDate date);

    int countByDueDate(LocalDate date);

    List<Task> findAllByOrderByCreatedAtDesc();     // for recent tasks
}

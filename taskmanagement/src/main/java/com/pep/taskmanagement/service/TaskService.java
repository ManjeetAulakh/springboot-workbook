package com.pep.taskmanagement.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.pep.taskmanagement.models.Task;
import com.pep.taskmanagement.models.Enums.TaskStatus;

public interface TaskService {

    Task createTask(Task task, Long pid, Long uid);

    Task updateTask(Long id, Task task);

    void deleteTask(Long id);

    List<Task> getAllTasks();

    Task getByID(Long id);

    // user and projet get functions

    Page<Task> getByUser(Long uid, int pageNumber, int pageSize);

    Page<Task> getByProject(Long pid, int pageNumber, int pageSize);

    Page<Task> getByUserAndStatus(Long uid, TaskStatus status, int pageNumber, int pageSize);

    Page<Task> getByProjectAndStatus(Long pid, TaskStatus status, int pageNumber, int pageSize);

    Page<Task> getByStatus(TaskStatus status, int pageNumber, int pageSize);

}

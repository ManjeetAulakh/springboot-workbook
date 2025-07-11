package com.pep.taskmanagement.service.Impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.pep.taskmanagement.exception.PageException;
import com.pep.taskmanagement.exception.ProjectException;
import com.pep.taskmanagement.exception.TaskException;
import com.pep.taskmanagement.exception.UserException;
import com.pep.taskmanagement.models.Task;
import com.pep.taskmanagement.models.Enums.TaskPriority;
import com.pep.taskmanagement.models.Enums.TaskStatus;
import com.pep.taskmanagement.repository.ProjectRepo;
import com.pep.taskmanagement.repository.TaskRepo;
import com.pep.taskmanagement.repository.UserRepo;
import com.pep.taskmanagement.service.TaskService;

public class TaskServiceImpl implements TaskService {

    private ProjectRepo projectRepo;
    private UserRepo userRepo;
    private TaskRepo taskRepo;

    public TaskServiceImpl(ProjectRepo projectRepo, UserRepo userRepo) {
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
    }

    private <T> boolean shouldUpdate(T newValue, T oldValue) {
        return !Objects.equals(newValue, oldValue);
    }

    @Override
    public Task createTask(Task task, Long pid, Long uid) {
        if (task.getTitle() == null) {
            throw new IllegalArgumentException("Task title not be Null!");
        }

        if (task.getDueDate() != null && task.getDueDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date cannot be in Past!");
        }

        projectRepo.findById(pid)
                .orElseThrow(() -> new ProjectException("Project not founf with this Project ID: " + pid));
        task.setProjectId(pid);

        if (uid != null) {
            userRepo.findById(uid)
                    .orElseThrow(() -> new UserException("User not found with this User ID: " + uid));
            task.setUserId(uid);
        } else {
            task.setUserId(null);
        }

        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.TO_DO); // deafult if not set in task object
        }
        if (task.getPriority() == null) {
            task.setPriority(TaskPriority.MEDIUM);
        }

        return taskRepo.save(task);
    }

    /*
     * | `task.getField()` (`a`) | `existing.getField()` (`b`) | `Objects.equals(a, b)` | `!Objects.equals(a, b)` | Update?                 | Explanation                             |
| ----------------------- | --------------------------- | ---------------------- | ----------------------- | ----------------------- | --------------------------------------- |
| `null`                  | `null`                      | ✅ `true`               | ❌ `false`               | ❌ Skip update           | Both values are `null`, no change.      |
| `null`                  | `10L`                       | ❌ `false`              | ✅ `true`                | ⚠️ Risky if not guarded | Trying to overwrite non-null with null. |
| `12L`                   | `12L`                       | ✅ `true`               | ❌ `false`               | ❌ Skip update           | Same value, no need to update.          |
| `15L`                   | `12L`                       | ❌ `false`              | ✅ `true`                | ✅ Update                | New value is different — update needed. |
| `12L`                   | `null`                      | ❌ `false`              | ✅ `true`                | ✅ Update                | Going from `null` to valid — update.    |

     * 
     */

    @Override
    public Task updateTask(Long id, Task task) {

        Task existingTask = taskRepo.findById(id)
                .orElseThrow(() -> new TaskException("Task not Exists with this ID: " + id));

        //  task.getUserId() != null checked bacuase cant check null in database by findById()
        // also != is wrong in object type bacause it compare refernce not value and !object hanlde every case and preventing to nulloverides
        if (task.getUserId() != null && !Objects.equals(task.getUserId(), existingTask.getUserId())) {
            userRepo.findById(task.getUserId())
                    .orElseThrow(() -> new UserException("User not exists with this ID: " + task.getUserId()));
            existingTask.setUserId(task.getUserId());
        }

        if (task.getProjectId() != null && !Objects.equals(task.getProjectId(), existingTask.getProjectId())) {
            projectRepo.findById(task.getProjectId())
                    .orElseThrow(() -> new ProjectException("Project not exists with this ID: " + task.getProjectId()));
            existingTask.setProjectId(task.getProjectId());
        }

        // Update other fields safely
        if (shouldUpdate(task.getTitle(), existingTask.getTitle())) {
            existingTask.setTitle(task.getTitle());
        }

        if (shouldUpdate(task.getDescription(), existingTask.getDescription())) {
            existingTask.setDescription(task.getDescription());
        }

        if (task.getDueDate() != null && !Objects.equals(task.getDueDate(), existingTask.getDueDate())) {
            if (task.getDueDate().isBefore(existingTask.getDueDate())) {
                throw new IllegalArgumentException("Date can not be in Past!");
            }
            existingTask.setDueDate(task.getDueDate());
        }

        if (shouldUpdate(task.getStatus(), existingTask.getStatus())) {
            existingTask.setStatus(task.getStatus());
        }

        if (shouldUpdate(task.getPriority(), existingTask.getPriority())) {
            existingTask.setPriority(task.getPriority());
        }
        return taskRepo.save(existingTask);

    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new TaskException("Task not exists with this ID: " + id));
        taskRepo.delete(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    @Override
    public Task getByID(Long id) {
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new TaskException("Task is not found with this Task ID: " + id));
        return task;
    }

    @Override
    public Page<Task> getByUser(Long uid, int pageNumber, int pageSize) {
        userRepo.findById(uid)
                .orElseThrow(() -> new UserException("User is not exists with this ID: " + uid));

        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Task> tasks = taskRepo.findByUserId(uid, page);

        return tasks;
    }

    @Override
    public Page<Task> getByProjectAndStatus(Long pid, TaskStatus status, int pageNumber, int pageSize) {
        projectRepo.findById(pid)
                .orElseThrow(() -> new PageException("Project is not exists with this ID: " + pid));

        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Task> tasks = taskRepo.findByProjectIdAndStatus(pid, status, page);

        return tasks;

    }

    @Override
    public Page<Task> getByUserAndStatus(Long uid, TaskStatus status, int pageNumber, int pageSize) {
        userRepo.findById(uid)
                .orElseThrow(() -> new UserException("User is not exists with this ID: " + uid));

        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Task> tasks = taskRepo.findByUserIdAndStatus(uid, status, page);

        return tasks;

    }

    @Override
    public Page<Task> getByStatus(TaskStatus status, int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Task> tasks = taskRepo.findByStatus(status, page);

        return tasks;
    }

    @Override
    public Page<Task> getByProject(Long pid, int pageNumber, int pageSize) {
        projectRepo.findById(pid)
                .orElseThrow(() -> new ProjectException("Project not exist with this ID: " + pid));
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Task> tasks = taskRepo.findByProjectId(pid, page);

        return tasks;
    }

}

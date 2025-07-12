package com.pep.taskmanagement.service;

import java.util.List;

import com.pep.taskmanagement.models.Project;

public interface ProjectService {
    
    Project createProject(Project project);
    List<Project> getAllProjects();
    List<Project> getByManager(Long uid);
    Project updateProject(Long pid, Project project);
    void deleteProject(Long pid);
}

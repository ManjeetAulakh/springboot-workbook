package com.pep.taskmanagement.service.Impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pep.taskmanagement.exception.ProjectException;
import com.pep.taskmanagement.models.Project;
import com.pep.taskmanagement.repository.ProjectRepo;
import com.pep.taskmanagement.repository.UserRepo;
import com.pep.taskmanagement.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepo projectRepo;
    private UserRepo userRepo;

    public ProjectServiceImpl(ProjectRepo projectRepo, UserRepo userRepo) {
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Project createProject(Project project) {
        if (project.getProjectManagerId() != null) {
            userRepo.findById(project.getProjectManagerId()).orElseThrow(
                    () -> new ProjectException(
                            "No Project Manager found with this ID: " + project.getProjectManagerId()));
        }

        if (project.getStartDate() == null || project.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("The Start Date must be today or in the future.");
        }

        if (project.getEndDate() == null || project.getEndDate().isBefore(project.getStartDate())) {
            throw new IllegalArgumentException("The End Date must be after the Start Date.");
        }
        return projectRepo.save(project);

    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepo.findAll();
    }

    @Override
    public List<Project> getByManager(Long uid) {
        if (uid != null) {
            userRepo.findById(uid).orElseThrow(
                    () -> new ProjectException(
                            "No Project Manager found with this ID: " + uid));
        }

        return projectRepo.findByProjectManagerId(uid);

    }

    @Override
    public Project updateProject(Long pid, Project updatedProject) {

        Project existingProject = projectRepo.findById(pid)
                .orElseThrow(() -> new ProjectException("Project not found with ID: " + pid));

        if (updatedProject.getName() != null) {
            existingProject.setName(updatedProject.getName());
        }

        if (updatedProject.getDescription() != null) {
            existingProject.setDescription(updatedProject.getDescription());
        }

        if (updatedProject.getStartDate() != null) {
            if (updatedProject.getStartDate().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Start date must be today or in the future.");
            }
            existingProject.setStartDate(updatedProject.getStartDate());
        }

        if (updatedProject.getEndDate() != null) {
            if (updatedProject.getStartDate() != null &&
                    updatedProject.getEndDate().isBefore(updatedProject.getStartDate())) {
                throw new IllegalArgumentException("End date must be after start date.");
            } else if (updatedProject.getStartDate() == null &&
                    updatedProject.getEndDate().isBefore(existingProject.getStartDate())) {
                throw new IllegalArgumentException("End date must be after start date.");
            }
            existingProject.setEndDate(updatedProject.getEndDate());
        }

        if (updatedProject.getProjectManagerId() != null) {
            userRepo.findById(updatedProject.getProjectManagerId())
                    .orElseThrow(() -> new ProjectException(
                            "No project manager found with ID: " + updatedProject.getProjectManagerId()));
            existingProject.setProjectManagerId(updatedProject.getProjectManagerId());
        }

        return projectRepo.save(existingProject);
    }

    @Override
    public void deleteProject(Long pid) {
        Project project = projectRepo.findById(pid)
                .orElseThrow(() -> new ProjectException("Project not found on this ID: " + pid));
        projectRepo.delete(project);
    }

}

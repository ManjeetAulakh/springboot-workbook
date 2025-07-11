package com.pep.taskmanagement.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pep.taskmanagement.models.Project;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {
    
    List<Project> findByProjectManagerId(long managerId);

    Page<Project> findByProjectManagerId(long managerId, Pageable pageable);

    List<Project> findByNameContainingIgnoreCase(String keyword);

    // ✅ 3. Count active projects (endDate is null or in the future)
    int countByEndDateIsNullOrEndDateAfter(LocalDate today);

    // ✅ 4. Get all projects ordered by creation date (recent first)
    List<Project> findAllByOrderByCreatedAtDesc();

    Page<Project> findAllByOrderByCreatedAtDesc(Pageable pageable);

}

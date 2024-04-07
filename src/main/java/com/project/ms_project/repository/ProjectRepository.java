package com.project.ms_project.repository;

import com.project.ms_project.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository extends JpaRepository<ProjectEntity,Long> {
    ProjectEntity findByProjectId(Long id);
}

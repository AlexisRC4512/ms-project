package com.project.ms_project.repository;

import com.project.ms_project.entity.ProjectUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectUserRepository extends JpaRepository<ProjectUserEntity,Long> {
    List<ProjectUserEntity> findAllByProject_ProjectId(Long projectId);
    ProjectUserEntity findByLeaderId(Long id);
    ProjectUserEntity findByMemberId(Long id);
    ProjectUserEntity findByEmail(String email);
    ProjectUserEntity findByProject_ProjectId(Long projectId);
}

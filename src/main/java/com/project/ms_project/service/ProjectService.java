package com.project.ms_project.service;

import com.project.ms_project.aggregates.request.UpdateProjectRequest;
import com.project.ms_project.aggregates.response.BaseResponse;
import com.project.ms_project.entity.ProjectEntity;
import com.project.ms_project.entity.ProjectUserEntity;

import java.util.List;

public interface ProjectService {
    BaseResponse projectCreate(Long idUser, ProjectEntity project);
    BaseResponse getProjectForId(Long id);
    BaseResponse updatePercentAdvance(Long id,int advace);
    BaseResponse updateProject(Long idUser, Long idProject, UpdateProjectRequest updateProjectRequest);
    BaseResponse deleteProject(Long idUser,Long idProject);
    BaseResponse addMemberProject(Long idUser,Long idProject,String emailMember);
    BaseResponse deleteMemberProject(Long idUser,Long idProject,String emailMember);
    List<ProjectUserEntity>getAllUser(Long idProject);
    boolean validateToken(String token);
    ProjectUserEntity getProjectUser(Long id);
    ProjectUserEntity getProjectMember(Long id);
    ProjectUserEntity getProjectEmail(String email);
}

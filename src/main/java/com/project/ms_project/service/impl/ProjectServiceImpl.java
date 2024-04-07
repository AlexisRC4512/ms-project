package com.project.ms_project.service.impl;

import com.project.ms_project.aggregates.constants.Constant;
import com.project.ms_project.aggregates.constants.RoleUser;
import com.project.ms_project.aggregates.request.UpdateProjectRequest;
import com.project.ms_project.aggregates.response.BaseResponse;
import com.project.ms_project.aggregates.response.UserResponse;
import com.project.ms_project.entity.ProjectEntity;
import com.project.ms_project.entity.ProjectUserEntity;
import com.project.ms_project.feingClient.UserClient;
import com.project.ms_project.repository.ProjectRepository;
import com.project.ms_project.repository.ProjectUserRepository;
import com.project.ms_project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectUserRepository projectUserRepository;
    private final UserClient userClient;

    @Override
    public BaseResponse projectCreate(Long idUser, ProjectEntity project) {
        try {
            UserResponse userResponse = getInfoUser(idUser);
            if (userResponse == null) {
                return new BaseResponse(Constant.CODE_USER_INCORRECT, Constant.MESS_USER_INCORRECT, Optional.empty());
            } else {
                LocalDateTime now = LocalDateTime.now();
                Timestamp timestamp = Timestamp.valueOf(now);
                ProjectEntity newProject = ProjectEntity.builder()
                        .projectCreatedBy(userResponse.getData().getFirstName() + " " + userResponse.getData().getLastName())
                        .name(project.getName()).dateCreated(timestamp)
                        .description(project.getDescription())
                        .state(1).startDate(project.getStartDate())
                        .deliveryDate(project.getDeliveryDate())
                        .percentageComplete(project.getPercentageComplete())
                        .build();
                ProjectEntity projectEntity = projectRepository.save(newProject);
                ProjectUserEntity projectUserEntity = ProjectUserEntity.builder()
                        .createdBy(newProject.getProjectCreatedBy())
                        .dateCreated(newProject.getDateCreated())
                        .email(userResponse.getData().getEmail())
                        .project(newProject)
                        .roleUser(RoleUser.LEADER.name())
                        .fullName(userResponse.getData().getFirstName() + " " + userResponse.getData().getLastName() + " " + userResponse.getData().getMiddleName())
                        .leaderId(userResponse.getData().getId())
                        .state(1)
                        .build();
                projectUserRepository.save(projectUserEntity);
                return new BaseResponse(Constant.CODE_SUCCESS, Constant.MESS_SUCCESS, Optional.of(projectEntity));
            }
        } catch (Exception exception) {
            return new BaseResponse(Constant.CODE_ERROR, Constant.MESS_ERROR, Optional.empty());
        }

    }

    @Override
    public BaseResponse getProjectForId(Long id) {
        try {
            ProjectEntity project = projectRepository.findByProjectId(id);
            if (project == null) {
                return new BaseResponse(Constant.CODE_USER_INCORRECT, Constant.MESS_ICORRECT_ID_PROJECT, Optional.empty());
            } else {
                return new BaseResponse(Constant.CODE_SUCCESS, Constant.MESS_SUCCESS, Optional.of(project));
            }
        } catch (Exception exception) {
            return new BaseResponse(Constant.CODE_ERROR, Constant.MESS_ERROR, Optional.empty());
        }


    }

    @Override
    public BaseResponse updatePercentAdvance(Long id, int advance) {
        try {
            ProjectEntity project = projectRepository.findByProjectId(id);
            if (project == null) {
                return new BaseResponse(Constant.CODE_USER_INCORRECT, Constant.MESS_ICORRECT_ID_PROJECT, Optional.empty());
            }
            if (advance > 100 || advance < 0) {
                return new BaseResponse(Constant.CODE_ERROR_ADVANCE_INCORRECT, Constant.MESS_ADVANCE_INCORRECT, Optional.empty());
            }
            project.setPercentageComplete(project.getPercentageComplete() + advance);
            return new BaseResponse(Constant.CODE_SUCCESS, Constant.MESS_SUCCESS, Optional.of(project));

        } catch (Exception exception) {
            return new BaseResponse(Constant.CODE_ERROR, exception.getMessage(), Optional.empty());
        }
    }

    @Override
    public BaseResponse updateProject(Long idUser, Long idProject, UpdateProjectRequest projectEntity) {
        try {
            UserResponse userResponse = getInfoUser(idUser);
            ProjectEntity project = projectRepository.findByProjectId(idProject);
            ProjectUserEntity projectUserEntity=projectUserRepository.findByLeaderId(idUser);
            if (userResponse == null) {
                return new BaseResponse(Constant.CODE_USER_INCORRECT, Constant.MESS_USER_INCORRECT, Optional.empty());
            }
            if (project == null) {
                return new BaseResponse(Constant.CODE_USER_INCORRECT, Constant.MESS_ICORRECT_ID_PROJECT, Optional.empty());
            } else {
                if (projectUserEntity!=null && projectUserEntity.getProject().getProjectId().equals(idProject)){
                    LocalDateTime now = LocalDateTime.now();
                    Timestamp timestamp = Timestamp.valueOf(now);
                    project.setName(projectEntity.getName());
                    project.setDescription(projectEntity.getDescription());
                    project.setState(projectEntity.getState());
                    project.setStartDate(projectEntity.getStartDate());
                    project.setDeliveryDate(projectEntity.getDeliveryDate());
                    project.setPercentageComplete(projectEntity.getPercentageComplete());
                    project.setProjectModifiedBy(userResponse.getData().getFirstName() + " " + userResponse.getData().getLastName());
                    project.setDateModified(timestamp);
                    projectRepository.save(project);
                    return new BaseResponse(Constant.CODE_SUCCESS, Constant.MESS_SUCCESS, Optional.of(project));
                }else {
                    return new BaseResponse(Constant.CODE_USER_NO_LEADER, Constant.MESS_USER_NO_LEADER, Optional.empty());
                }

            }
        } catch (Exception exception) {
            return new BaseResponse(Constant.CODE_ERROR, Constant.MESS_ERROR, Optional.empty());
        }
    }

    @Override
    public BaseResponse deleteProject(Long idUser, Long idProject) {
        try {
            UserResponse userResponse = getInfoUser(idUser);
            ProjectEntity project = projectRepository.findByProjectId(idProject);
            ProjectUserEntity projectUserEntity=projectUserRepository.findByLeaderId(idUser);
            if (userResponse == null) {
                return new BaseResponse(Constant.CODE_USER_INCORRECT, Constant.MESS_USER_INCORRECT, Optional.empty());
            }
            if (project == null) {
                return new BaseResponse(Constant.CODE_USER_INCORRECT, Constant.MESS_ICORRECT_ID_PROJECT, Optional.empty());
            } else {
                if (projectUserEntity!=null && projectUserEntity.getProject().getProjectId().equals(idProject)){
                LocalDateTime now = LocalDateTime.now();
                Timestamp timestamp = Timestamp.valueOf(now);
                project.setState(0);
                project.setProjectDeletedBy(userResponse.getData().getFirstName() + " " + userResponse.getData().getLastName());
                project.setDateDeleted(timestamp);
                projectRepository.save(project);
                return new BaseResponse(Constant.CODE_SUCCESS, Constant.MESS_SUCCESS, Optional.of(project));
                }else {
                    return new BaseResponse(Constant.CODE_USER_NO_LEADER, Constant.MESS_USER_NO_LEADER, Optional.empty());
                }
            }
        } catch (Exception exception) {
            return new BaseResponse(Constant.CODE_ERROR, exception.getMessage(), Optional.empty());
        }
    }

    @Override
    public BaseResponse addMemberProject(Long idUser, Long idProject, String emailMember) {
        try {
            ProjectUserEntity projectUserEntity = projectUserRepository.findByLeaderId(idUser);
            ProjectEntity project = projectRepository.findByProjectId(idProject);
            UserResponse userResponse = getInfoMemeber(emailMember);
            if (projectUserEntity == null) {
                return new BaseResponse(Constant.CODE_USER_INCORRECT, Constant.MESS_USER_INCORRECT, Optional.empty());
            }
            if (project == null) {
                return new BaseResponse(Constant.CODE_USER_INCORRECT, Constant.MESS_ICORRECT_ID_PROJECT, Optional.empty());
            }
            if (userResponse == null) {
                return new BaseResponse(Constant.CODE_EMAIL_NOT_EXIST, Constant.MESS_EMAIL_NOT_EXIST, Optional.empty());
            } else {
                if (projectUserEntity!=null && projectUserEntity.getProject().getProjectId().equals(idProject)){
                LocalDateTime now = LocalDateTime.now();
                Timestamp timestamp = Timestamp.valueOf(now);
                ProjectUserEntity projectUser = ProjectUserEntity.builder()
                        .createdBy(projectUserEntity.getFullName())
                        .dateCreated(timestamp)
                        .email(userResponse.getData().getEmail())
                        .project(project).roleUser(RoleUser.MEMBER.name())
                        .fullName(userResponse.getData().getFirstName() + " " + userResponse.getData().getLastName() + " " + userResponse.getData().getMiddleName())
                        .memberId(userResponse.getData().getId())
                        .state(1)
                        .build();
                projectUserRepository.save(projectUser);
                return new BaseResponse(Constant.CODE_SUCCESS, Constant.MESS_SUCCESS, Optional.of(projectUser));
                }else {
                    return new BaseResponse(Constant.CODE_USER_NO_LEADER,Constant.MESS_USER_NO_LEADER, Optional.empty());
                }
            }
        } catch (Exception exception) {
            return new BaseResponse(Constant.CODE_ERROR, Constant.MESS_ERROR, Optional.empty());
        }
    }

    @Override
    public BaseResponse deleteMemberProject(Long idUser, Long idProject, String emailMember) {
        try {
            ProjectUserEntity projectUserEntity = projectUserRepository.findByLeaderId(idUser);
            ProjectEntity project = projectRepository.findByProjectId(idProject);
            ProjectUserEntity deleteResponse = projectUserRepository.findByEmail(emailMember);
            if (projectUserEntity == null) {
                return new BaseResponse(Constant.CODE_USER_INCORRECT, Constant.MESS_USER_INCORRECT, Optional.empty());
            }
            if (project == null) {
                return new BaseResponse(Constant.CODE_USER_INCORRECT, Constant.MESS_ICORRECT_ID_PROJECT, Optional.empty());
            }
            if (deleteResponse == null) {
                return new BaseResponse(Constant.CODE_EMAIL_NOT_EXIST, Constant.MESS_EMAIL_NOT_EXIST, Optional.empty());
            } else {
                if (projectUserEntity!=null && projectUserEntity.getProject().getProjectId().equals(idProject)){
                LocalDateTime now = LocalDateTime.now();
                Timestamp timestamp = Timestamp.valueOf(now);
                deleteResponse.setDeletedBy(projectUserEntity.getFullName());
                deleteResponse.setDateDeleted(timestamp);
                deleteResponse.setState(0);
                projectUserRepository.save(deleteResponse);
                return new BaseResponse(Constant.CODE_SUCCESS, Constant.MESS_SUCCESS, Optional.of(deleteResponse));
                }else {
                    return new BaseResponse(Constant.CODE_USER_NO_LEADER, Constant.MESS_USER_NO_LEADER, Optional.empty());
                }
            }
        } catch (Exception exception) {
            return new BaseResponse(Constant.CODE_ERROR,Constant.MESS_ERROR, Optional.empty());
        }
    }

    @Override
    public List<ProjectUserEntity> getAllUser(Long idProject) {
        try {
            List<ProjectUserEntity> projectUserEntities = projectUserRepository.findAllByProject_ProjectId(idProject);
            if (projectUserEntities.isEmpty()) {
                return List.of();
            } else {
                return projectUserEntities;
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return List.of();
        }

    }
    @Override
    public boolean validateToken(String token)
    {
        try {
            if (token.isEmpty())
            {
                return false;
            }
            return userClient.validateApiToken(token);
        }catch (Exception exception)
        {
            log.error(exception.getMessage());
            return false;
        }
    }

    @Override
    public ProjectUserEntity getProjectUser(Long id) {
       try {
           ProjectUserEntity projectUserEntity=projectUserRepository.findByLeaderId(id);
           if (projectUserEntity!=null){
               return projectUserEntity;
           }else {
               return null;
           }
       }catch (Exception exception)
       {
           log.error(exception.getMessage());
            return null;
       }
    }

    @Override
    public ProjectUserEntity getProjectMember(Long id) {
        try {
            ProjectUserEntity projectUserEntity=projectUserRepository.findByMemberId(id);
            if (projectUserEntity!=null){
                return projectUserEntity;
            }else {
                return null;
            }
        }catch (Exception exception)
        {
            log.error(exception.getMessage());
            return null;
        }
    }

    @Override
    public ProjectUserEntity getProjectEmail(String email) {
        try {
            ProjectUserEntity projectMember=projectUserRepository.findByEmail(email);
            if (projectMember!=null){
                return projectMember;
            }else {
                return null;
            }
        }catch (Exception exception)
        {
            log.error(exception.getMessage());
            return null;
        }
    }

    private UserResponse getInfoUser(Long id){
        try {
            UserResponse response=userClient.getUser(id);
            if (response!=null)
            {
                return response;
            }else {
                return null;
            }
        }catch (Exception exception)
        {log.error(exception.getMessage());
            return null;
        }
    }
    private UserResponse getInfoMemeber(String email){
        try {
            UserResponse response=userClient.getUserForEmail(email);
            if (response!=null)
            {
                return response;
            }else {
                return null;
            }
        }catch (Exception exception)
        {log.error(exception.getMessage());
            return null;
        }
    }
}

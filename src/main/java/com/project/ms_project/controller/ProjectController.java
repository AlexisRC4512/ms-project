package com.project.ms_project.controller;


import com.project.ms_project.aggregates.constants.Constant;
import com.project.ms_project.aggregates.request.UpdateProjectRequest;
import com.project.ms_project.aggregates.response.BaseResponse;
import com.project.ms_project.entity.ProjectEntity;
import com.project.ms_project.entity.ProjectUserEntity;
import com.project.ms_project.service.ProjectService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@OpenAPIDefinition(
        info = @Info(
                title = "API-project",
                version = "1.0",
                description = "Api de project"
        )
)
@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    @PostMapping("/{id}")
    public BaseResponse projectCreate(@PathVariable Long id,
                                      @RequestBody ProjectEntity project,
                                      @RequestParam String authorizationHeader) {
        if (projectService.validateToken(authorizationHeader))
        {
            return projectService.projectCreate(id, project);
        }else {
            return new BaseResponse(Constant.CODE_NOT_AUTORIZED,Constant.MESS_NOT_AUTORIZED, Optional.empty());
        }

    }
    @GetMapping("/get/{id}")
    public BaseResponse getProjectForId(@PathVariable("id") Long id,   @RequestParam String authorizationHeader)
    {
        if (projectService.validateToken(authorizationHeader))
        {
            return projectService.getProjectForId(id);
        }else{
            return new BaseResponse(Constant.CODE_NOT_AUTORIZED,Constant.MESS_NOT_AUTORIZED, Optional.empty());
        }
    }
    @PutMapping("/update/{id}")
    public BaseResponse updatePercentAdvance(@PathVariable Long id,
                                             @RequestParam int advance,
                                             @RequestParam String authorizationHeader) {
        if (projectService.validateToken(authorizationHeader)) {
            return projectService.updatePercentAdvance(id, advance);
        } else {
            return new BaseResponse(Constant.CODE_NOT_AUTORIZED,Constant.MESS_NOT_AUTORIZED, Optional.empty());
        }
    }
    @PutMapping("/edit/{idUser}/{idProject}")
    public BaseResponse updateProject(@PathVariable Long idUser,
                                      @PathVariable Long idProject,
                                      @RequestBody UpdateProjectRequest projectEntity,
                                      @RequestParam String authorizationHeader) {
        if (projectService.validateToken(authorizationHeader)) {
            return projectService.updateProject(idUser, idProject, projectEntity);
        } else {
            return new BaseResponse(Constant.CODE_NOT_AUTORIZED,Constant.MESS_NOT_AUTORIZED, Optional.empty());
        }
    }
    @DeleteMapping("/delete/{idUser}/{idProject}")
    public BaseResponse deleteProject(@PathVariable Long idUser,
                                      @PathVariable Long idProject,
                                      @RequestParam String authorizationHeader) {
        if (projectService.validateToken(authorizationHeader)) {
            return projectService.deleteProject(idUser, idProject);
        } else {
            return new BaseResponse(Constant.CODE_NOT_AUTORIZED,Constant.MESS_NOT_AUTORIZED, Optional.empty());
        }
    }
    @PostMapping("/addMember/{idUser}/{idProject}")
    public BaseResponse addMemberProject(@PathVariable Long idUser,
                                         @PathVariable Long idProject,
                                         @RequestParam String emailMember,
                                         @RequestParam String authorizationHeader) {
        if (projectService.validateToken(authorizationHeader)) {
            return projectService.addMemberProject(idUser, idProject, emailMember);
        } else {
            return new BaseResponse(Constant.CODE_NOT_AUTORIZED,Constant.MESS_NOT_AUTORIZED, Optional.empty());
        }
    }
    @DeleteMapping("/deleteMember/{idUser}/{idProject}")
    public BaseResponse deleteMemberProject(@PathVariable Long idUser,
                                            @PathVariable Long idProject,
                                            @RequestParam String emailMember,
                                            @RequestParam String authorizationHeader) {
        if (projectService.validateToken(authorizationHeader)) {
            return projectService.deleteMemberProject(idUser, idProject, emailMember);
        } else {
            return new BaseResponse(Constant.CODE_NOT_AUTORIZED,Constant.MESS_NOT_AUTORIZED, Optional.empty());
        }
    }
    @GetMapping("/projects/{idProject}/users")
    public List<ProjectUserEntity> getAllUsersByProjectId(@PathVariable Long idProject , @RequestParam String authorizationHeader) {
        if (projectService.validateToken(authorizationHeader)) {
            return projectService.getAllUser(idProject);
        } else {
            return List.of();
        }
    }
    @GetMapping("/leaderProject/{id}")
    public ProjectUserEntity getProjectUser(@PathVariable Long id) {
            return projectService.getProjectUser(id);

    }
    @GetMapping("/memberProject/{id}")
    public ProjectUserEntity getProjectMember(@PathVariable Long id) {
        return projectService.getProjectMember(id);

    }
    @GetMapping("/memberProject")
    public ProjectUserEntity getProjectUser(@RequestParam String email) {
        return projectService.getProjectEmail(email);

    }
}

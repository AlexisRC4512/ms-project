package com.project.ms_project;

import com.project.ms_project.aggregates.constants.Constant;
import com.project.ms_project.aggregates.request.UpdateProjectRequest;
import com.project.ms_project.aggregates.response.BaseResponse;
import com.project.ms_project.aggregates.response.UserResponse;
import com.project.ms_project.aggregates.util.Data;
import com.project.ms_project.entity.ProjectEntity;
import com.project.ms_project.entity.ProjectUserEntity;
import com.project.ms_project.feingClient.UserClient;
import com.project.ms_project.repository.ProjectRepository;
import com.project.ms_project.repository.ProjectUserRepository;
import com.project.ms_project.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectUserRepository projectUserRepository;
    @InjectMocks
    private ProjectServiceImpl projectService;
    @Mock
    private UserClient userClient;
    private ProjectEntity project;
    private ProjectUserEntity projectUser;
    private UserResponse userResponse;
    private Data data;
    @BeforeEach
    void setUp(){
        project=ProjectEntity.builder().projectId(1L)
                .name("Proyecto 1")
                .description("Descripción del Proyecto 1")
                .startDate(Timestamp.valueOf(LocalDateTime.now()))
                .deliveryDate(Timestamp.valueOf(LocalDateTime.now().plusDays(30)))
                .state(1)
                .percentageComplete(0)
                .projectCreatedBy("Usuario1")
                .dateCreated(Timestamp.valueOf(LocalDateTime.now())).build();
        projectUser = ProjectUserEntity.builder()
                .projectUserId(1L)
                .roleUser("Líder")
                .state(1)
                .leaderId(1L)
                .memberId(2L)
                .project(project)
                .email("usuario@example.com")
                .fullName("Nombre Apellido")
                .createdBy(project.getProjectCreatedBy())
                .dateCreated(project.getDateCreated())
                .build();
        data = Data.builder()
                .id(1L)
                .numDoc("123456789")
                .password("password123")
                .email("usuario@example.com")
                .firstName("Nombre")
                .lastName("Apellido")
                .middleName("SegundoNombre")
                .roleUser(List.of("ROLE_ADMIN", "ROLE_USER"))
                .build();

        userResponse = UserResponse.builder()
                .code(200)
                .message("Success")
                .data(data)
                .build();
    }
    @Test
    void testValidateToken() {
        String token = "token";
        when(userClient.validateApiToken(token)).thenReturn(true);
        Boolean result = projectService.validateToken(token);
        assertTrue(result);
        verify(userClient).validateApiToken(token);
    }
    @Test
    void testValidateTokenWithEmptyToken() {
        String token = "";
        Boolean result = projectService.validateToken(token);
        assertThat(result).isFalse();
        verify(userClient, never()).validateApiToken(anyString());
    }

    @Test
    void testValidateTokenWithException() {
        String token = "tokeninvalido";
        when(userClient.validateApiToken(token)).thenThrow();
        Boolean result = projectService.validateToken(token);
        assertThat(result).isFalse();
        verify(userClient).validateApiToken(token);
    }

    @Test
    void createProjectTest_Success(){
        Long id=1L;
       when(userClient.getUser(id)).thenReturn(userResponse);
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(project);
        BaseResponse response=projectService.projectCreate(id,project);
        assertEquals(Constant.MESS_SUCCESS,response.getMessage());
        assertEquals(Constant.CODE_SUCCESS,response.getCode());
        assertEquals(project,response.getData().get());
    }
    @Test
    void createProjectTest_ID_ERROR(){
        Long idError=2L;
        when(userClient.getUser(idError)).thenReturn(null);
        BaseResponse response=projectService.projectCreate(idError,project);
        assertEquals(Constant.CODE_USER_INCORRECT,response.getCode());
        assertEquals(Constant.MESS_USER_INCORRECT,response.getMessage());
    }
    @Test
    void createProjectTest_ERROR(){
        Long idCreateError=2L;
        when(userClient.getUser(idCreateError)).thenReturn(userResponse);
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(null);
        BaseResponse response=projectService.projectCreate(idCreateError,project);
        assertEquals(Constant.CODE_ERROR,response.getCode());
        assertEquals(Constant.MESS_ERROR,response.getMessage());
    }
    @Test
    void getProjectForIdTest(){
        Long IdGet=1L;
        when(projectRepository.findByProjectId(IdGet)).thenReturn(project);
        BaseResponse response=projectService.getProjectForId(IdGet);
        assertEquals(Constant.MESS_SUCCESS,response.getMessage());
        assertEquals(Constant.CODE_SUCCESS,response.getCode());
        assertEquals(project,response.getData().get());
    }
    @Test
    void getProjectForIdTestNull(){
        Long IdGetNull=2L;
        when(projectRepository.findByProjectId(IdGetNull)).thenReturn(null);
        BaseResponse response=projectService.getProjectForId(IdGetNull);
        assertEquals(Constant.MESS_ICORRECT_ID_PROJECT,response.getMessage());
        assertEquals(Constant.CODE_USER_INCORRECT,response.getCode());
    }
    @Test
    void getProjectForIdTestError(){
        Long IdGet=1L;
        when(projectRepository.findByProjectId(IdGet)).thenReturn(project);
        when(projectService.getProjectForId(IdGet)).thenThrow();
        BaseResponse response=projectService.getProjectForId(IdGet);
        assertEquals(Constant.MESS_ERROR,response.getMessage());
        assertEquals(Constant.CODE_ERROR,response.getCode());
    }
    @Test
    void updatePercentAdvanceTest(){
        Long idAdvance=1L;
        int advance=60;
        when(projectRepository.findByProjectId(idAdvance)).thenReturn(project);
        BaseResponse response=projectService.updatePercentAdvance(idAdvance,advance);
        assertEquals(Constant.MESS_SUCCESS,response.getMessage());
        assertEquals(Constant.CODE_SUCCESS,response.getCode());
        assertEquals(project,response.getData().get());
    }
    @Test
    void updatePercentAdvanceTestNull(){
        Long idAdvance=1L;
        int advance=60;
        when(projectRepository.findByProjectId(idAdvance)).thenReturn(null);
        BaseResponse response=projectService.updatePercentAdvance(idAdvance,advance);
        assertEquals(Constant.MESS_ICORRECT_ID_PROJECT,response.getMessage());
        assertEquals(Constant.CODE_USER_INCORRECT,response.getCode());
    }
    @Test
    void updatePercentAdvanceTestMore100Error(){
        Long idAdvance=1L;
        int advance=600;
        when(projectRepository.findByProjectId(idAdvance)).thenReturn(project);
        BaseResponse response=projectService.updatePercentAdvance(idAdvance,advance);
        assertEquals(Constant.MESS_ADVANCE_INCORRECT,response.getMessage());
        assertEquals(Constant.CODE_ERROR_ADVANCE_INCORRECT,response.getCode());
    }
    @Test
    void updateProjectTest(){
        Long idUpadte=1L;
        Long idProject=1L;
        UpdateProjectRequest request = UpdateProjectRequest.builder()
                .name("Nombre del Proyecto")
                .description("Descripción del Proyecto")
                .startDate(Timestamp.valueOf(LocalDateTime.now()))
                .deliveryDate(Timestamp.valueOf(LocalDateTime.now().plusDays(30)))
                .state(1)
                .percentageComplete(50)
                .build();
        when(userClient.getUser(idUpadte)).thenReturn(userResponse);
        when(projectRepository.findByProjectId(idProject)).thenReturn(project);
        when(projectUserRepository.findByLeaderId(idUpadte)).thenReturn(projectUser);
        BaseResponse response=projectService.updateProject(idUpadte,idProject,request);
        assertEquals(Constant.MESS_SUCCESS,response.getMessage());
        assertEquals(Constant.CODE_SUCCESS,response.getCode());
        assertEquals(project,response.getData().get());
    }
    @Test
    void updateProjectTestUserNull(){
        Long idUpadte=1L;
        Long idProject=1L;
        UpdateProjectRequest request = UpdateProjectRequest.builder()
                .name("Nombre del Proyecto")
                .description("Descripción del Proyecto")
                .startDate(Timestamp.valueOf(LocalDateTime.now()))
                .deliveryDate(Timestamp.valueOf(LocalDateTime.now().plusDays(30)))
                .state(1)
                .percentageComplete(50)
                .build();
        when(userClient.getUser(idUpadte)).thenReturn(null);
        BaseResponse response=projectService.updateProject(idUpadte,idProject,request);
        assertEquals(Constant.MESS_USER_INCORRECT,response.getMessage());
        assertEquals(Constant.CODE_USER_INCORRECT,response.getCode());
    }
    @Test
    void updateProjectTestProjectNull(){
        Long idUpadte=1L;
        Long idProject=1L;
        UpdateProjectRequest request = UpdateProjectRequest.builder()
                .name("Nombre del Proyecto")
                .description("Descripción del Proyecto")
                .startDate(Timestamp.valueOf(LocalDateTime.now()))
                .deliveryDate(Timestamp.valueOf(LocalDateTime.now().plusDays(30)))
                .state(1)
                .percentageComplete(50)
                .build();
        when(userClient.getUser(idUpadte)).thenReturn(userResponse);
        when(projectRepository.findByProjectId(idProject)).thenReturn(null);
        BaseResponse response=projectService.updateProject(idUpadte,idProject,request);
        assertEquals(Constant.MESS_ICORRECT_ID_PROJECT,response.getMessage());
        assertEquals(Constant.CODE_USER_INCORRECT,response.getCode());
    }
    @Test
    void updateProjectTestUserProjectNull(){
        Long idUpadte=1L;
        Long idProject=1L;
        UpdateProjectRequest request = UpdateProjectRequest.builder()
                .name("Nombre del Proyecto")
                .description("Descripción del Proyecto")
                .startDate(Timestamp.valueOf(LocalDateTime.now()))
                .deliveryDate(Timestamp.valueOf(LocalDateTime.now().plusDays(30)))
                .state(1)
                .percentageComplete(50)
                .build();
        when(userClient.getUser(idUpadte)).thenReturn(userResponse);
        when(projectRepository.findByProjectId(idProject)).thenReturn(project);
        when(projectUserRepository.findByLeaderId(idUpadte)).thenReturn(null);
        BaseResponse response=projectService.updateProject(idUpadte,idProject,request);
        assertEquals(Constant.MESS_USER_NO_LEADER,response.getMessage());
        assertEquals(Constant.CODE_USER_NO_LEADER,response.getCode());
    }
    @Test
    void DeleteProjectTest(){
        Long idDelete=1L;
        Long idProjectDelete=1L;
        when(userClient.getUser(idDelete)).thenReturn(userResponse);
        when(projectRepository.findByProjectId(idProjectDelete)).thenReturn(project);
        when(projectUserRepository.findByLeaderId(idDelete)).thenReturn(projectUser);
        BaseResponse response=projectService.deleteProject(idProjectDelete,idProjectDelete);
        assertEquals(Constant.MESS_SUCCESS,response.getMessage());
        assertEquals(Constant.CODE_SUCCESS,response.getCode());
        assertEquals(project,response.getData().get());
    }
    @Test
    void DeleteProjectTestUserNull(){
        Long idDelete=1L;
        Long idProjectDelete=1L;
        when(userClient.getUser(idDelete)).thenReturn(null);
        BaseResponse response=projectService.deleteProject(idProjectDelete,idProjectDelete);
        assertEquals(Constant.MESS_USER_INCORRECT,response.getMessage());
        assertEquals(Constant.CODE_USER_INCORRECT,response.getCode());
    }
    @Test
    void DeleteProjectTestProjectNull(){
        Long idDelete=1L;
        Long idProjectDelete=1L;
        when(userClient.getUser(idDelete)).thenReturn(userResponse);
        when(projectRepository.findByProjectId(idProjectDelete)).thenReturn(null);
        BaseResponse response=projectService.deleteProject(idProjectDelete,idProjectDelete);
        assertEquals(Constant.MESS_ICORRECT_ID_PROJECT,response.getMessage());
        assertEquals(Constant.CODE_USER_INCORRECT,response.getCode());
    }
    @Test
    void DeleteProjectTestUserProjectNull(){
        Long idDelete=1L;
        Long idProjectDelete=1L;
        when(userClient.getUser(idDelete)).thenReturn(userResponse);
        when(projectRepository.findByProjectId(idProjectDelete)).thenReturn(project);
        when(projectUserRepository.findByLeaderId(idDelete)).thenReturn(null);
        BaseResponse response=projectService.deleteProject(idProjectDelete,idProjectDelete);
        assertEquals(Constant.MESS_USER_NO_LEADER,response.getMessage());
        assertEquals(Constant.CODE_USER_NO_LEADER,response.getCode());
    }
    @Test
    void testGetProjectUser_Success() {
        Long userId = 123L;
        ProjectUserEntity mockProjectUser = new ProjectUserEntity();
        mockProjectUser.setLeaderId(userId);
        when(projectUserRepository.findByLeaderId(userId)).thenReturn(mockProjectUser);
        ProjectUserEntity result = projectService.getProjectUser(userId);
        assertNotNull(result);
        assertEquals(userId, result.getLeaderId());
        verify(projectUserRepository, times(1)).findByLeaderId(userId);
    }

    @Test
    void testGetProjectUser_NotFound() {
        Long userId = 456L;
        when(projectUserRepository.findByLeaderId(userId)).thenReturn(null);
        ProjectUserEntity result = projectService.getProjectUser(userId);
        assertNull(result);
        verify(projectUserRepository, times(1)).findByLeaderId(userId);
    }

    @Test
    void testGetProjectUser_Exception() {
        Long userId = 789L;
        when(projectUserRepository.findByLeaderId(userId)).thenThrow(new RuntimeException("Test exception"));
        ProjectUserEntity result = projectService.getProjectUser(userId);
        assertNull(result);
        verify(projectUserRepository, times(1)).findByLeaderId(userId);
    }
    @Test
    void testGetProjectEmail_Success() {
        String email = "john.doe@example.com";
        ProjectUserEntity mockProjectMember = new ProjectUserEntity();
        mockProjectMember.setEmail(email);
        when(projectUserRepository.findByEmail(email)).thenReturn(mockProjectMember);
        ProjectUserEntity result = projectService.getProjectEmail(email);
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(projectUserRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetProjectEmail_NotFound() {
        String email = "nonexistent@example.com";
        when(projectUserRepository.findByEmail(email)).thenReturn(null);
        ProjectUserEntity result = projectService.getProjectEmail(email);
        assertNull(result);
        verify(projectUserRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetProjectEmail_Exception() {
        String email = "error@example.com";
        when(projectUserRepository.findByEmail(email)).thenThrow(new RuntimeException("Test exception"));
        ProjectUserEntity result = projectService.getProjectEmail(email);
        assertNull(result);
        verify(projectUserRepository, times(1)).findByEmail(email);
    }
    @Test
    void testGetProjectMember_Success() {
        Long memberId = 123L;
        ProjectUserEntity mockProjectMember = new ProjectUserEntity();
        mockProjectMember.setMemberId(memberId);

        when(projectUserRepository.findByMemberId(memberId)).thenReturn(mockProjectMember);
        ProjectUserEntity result = projectService.getProjectMember(memberId);
        assertNotNull(result);
        assertEquals(memberId, result.getMemberId());
        verify(projectUserRepository, times(1)).findByMemberId(memberId);
    }

    @Test
    void testGetProjectMember_NotFound() {
        Long memberId = 456L;
        when(projectUserRepository.findByMemberId(memberId)).thenReturn(null);
        ProjectUserEntity result = projectService.getProjectMember(memberId);
        assertNull(result);
        verify(projectUserRepository, times(1)).findByMemberId(memberId);
    }

    @Test
    void testGetProjectMember_Exception() {
        Long memberId = 789L;
        when(projectUserRepository.findByMemberId(memberId)).thenThrow(new RuntimeException("Test exception"));
        ProjectUserEntity result = projectService.getProjectMember(memberId);
        assertNull(result);
        verify(projectUserRepository, times(1)).findByMemberId(memberId);
    }
    @Test
    void testGetAllUser_Success() {
        Long projectId = 123L;
        List<ProjectUserEntity> mockProjectUsers = new ArrayList<>();
        mockProjectUsers.add(new ProjectUserEntity());
        mockProjectUsers.add(new ProjectUserEntity());
        when(projectUserRepository.findAllByProject_ProjectId(projectId)).thenReturn(mockProjectUsers);
        List<ProjectUserEntity> result = projectService.getAllUser(projectId);
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(projectUserRepository, times(1)).findAllByProject_ProjectId(projectId);
    }

    @Test
    void testGetAllUser_EmptyList() {
        Long projectId = 456L;
        when(projectUserRepository.findAllByProject_ProjectId(projectId)).thenReturn(new ArrayList<>());
        List<ProjectUserEntity> result = projectService.getAllUser(projectId);
        assertEquals(result,List.of());
        verify(projectUserRepository, times(1)).findAllByProject_ProjectId(projectId);
    }

    @Test
    void testGetAllUser_Exception() {
        Long projectId = 789L;
        when(projectUserRepository.findAllByProject_ProjectId(projectId)).thenThrow(new RuntimeException("Test exception"));
        List<ProjectUserEntity> result = projectService.getAllUser(projectId);
        assertEquals(result,List.of());
        verify(projectUserRepository, times(1)).findAllByProject_ProjectId(projectId);
    }
    @Test
    void testAddMemberProject(){
        Long idAddMember=1L;
        Long idProject=1L;
        String emailUser="usuario@example.com";
        when(userClient.getUserForEmail(emailUser)).thenReturn(userResponse);
        when(projectUserRepository.findByLeaderId(idAddMember)).thenReturn(projectUser);
        when(projectRepository.findByProjectId(idProject)).thenReturn(project);
        BaseResponse baseResponse =projectService.addMemberProject(idAddMember,idProject,emailUser);
        assertEquals(Constant.CODE_SUCCESS,baseResponse.getCode());
        assertEquals(Constant.MESS_SUCCESS,baseResponse.getMessage());
    }
    @Test
    void testAddMemberProjectUserNull(){
        Long idAddMember=1L;
        Long idProject=1L;
        String emailUser="usuario@example.com";
        when(userClient.getUserForEmail(emailUser)).thenReturn(null);
        when(projectUserRepository.findByLeaderId(idAddMember)).thenReturn(projectUser);
        when(projectRepository.findByProjectId(idProject)).thenReturn(project);
        BaseResponse baseResponse =projectService.addMemberProject(idAddMember,idProject,emailUser);
        assertEquals(Constant.CODE_EMAIL_NOT_EXIST,baseResponse.getCode());
        assertEquals(Constant.MESS_EMAIL_NOT_EXIST,baseResponse.getMessage());
    }
    @Test
    void testAddMemberProjectUserLeaderNull(){
        Long idAddMember=1L;
        Long idProject=1L;
        String emailUser="usuario@example.com";
        when(projectUserRepository.findByLeaderId(idAddMember)).thenReturn(null);
        when(projectRepository.findByProjectId(idProject)).thenReturn(project);
        BaseResponse baseResponse =projectService.addMemberProject(idAddMember,idProject,emailUser);
        assertEquals(Constant.CODE_USER_INCORRECT,baseResponse.getCode());
        assertEquals(Constant.MESS_USER_INCORRECT,baseResponse.getMessage());
    }
    @Test
    void testAddMemberProjectUserProjectNull(){
        Long idAddMember=1L;
        Long idProject=1L;
        String emailUser="usuario@example.com";
        when(projectUserRepository.findByLeaderId(idAddMember)).thenReturn(projectUser);
        when(projectRepository.findByProjectId(idProject)).thenReturn(null);
        BaseResponse baseResponse =projectService.addMemberProject(idAddMember,idProject,emailUser);
        assertEquals(Constant.CODE_USER_INCORRECT,baseResponse.getCode());
        assertEquals(Constant.MESS_ICORRECT_ID_PROJECT,baseResponse.getMessage());
    }
    @Test
    void testAddMemberProjectUserError(){
        Long idAddMember=1L;
        Long idProject=1L;
        String emailUser="usuario@example.com";
        when(userClient.getUserForEmail(emailUser)).thenReturn(null);
        when(projectUserRepository.findByLeaderId(idAddMember)).thenReturn(projectUser);
        when(projectRepository.findByProjectId(idProject)).thenReturn(project);
        when(projectService.addMemberProject(idAddMember,idProject,emailUser)).thenThrow();
        BaseResponse baseResponse =projectService.addMemberProject(idAddMember,idProject,emailUser);
        assertEquals(Constant.CODE_ERROR,baseResponse.getCode());
        assertEquals(Constant.MESS_ERROR,baseResponse.getMessage());
    }
    @Test
    void testDeleteMemberProject(){
        Long idAddMember=1L;
        Long idProject=1L;
        String emailUser="usuario@example.com";
        when(projectUserRepository.findByLeaderId(idAddMember)).thenReturn(projectUser);
        when(projectUserRepository.findByEmail(emailUser)).thenReturn(projectUser);
        when(projectRepository.findByProjectId(idProject)).thenReturn(project);
        BaseResponse baseResponse =projectService.deleteMemberProject(idAddMember,idProject,emailUser);
        assertEquals(Constant.CODE_SUCCESS,baseResponse.getCode());
        assertEquals(Constant.MESS_SUCCESS,baseResponse.getMessage());
    }
    @Test
    void testDelteMemberProjectUserNull(){
        Long idAddMember=1L;
        Long idProject=1L;
        String emailUser="usuario@example.com";
        when(projectUserRepository.findByLeaderId(idAddMember)).thenReturn(null);
        when(projectUserRepository.findByEmail(emailUser)).thenReturn(projectUser);
        when(projectRepository.findByProjectId(idProject)).thenReturn(project);
        BaseResponse baseResponse =projectService.deleteMemberProject(idAddMember,idProject,emailUser);
        assertEquals(Constant.CODE_USER_INCORRECT,baseResponse.getCode());
        assertEquals(Constant.MESS_USER_INCORRECT,baseResponse.getMessage());
    }
    @Test
    void testDeleteMemberProjectUserProjectNull(){
        Long idAddMember=1L;
        Long idProject=1L;
        String emailUser="usuario@example.com";
        when(projectUserRepository.findByLeaderId(idAddMember)).thenReturn(projectUser);
        when(projectUserRepository.findByEmail(emailUser)).thenReturn(projectUser);
        when(projectRepository.findByProjectId(idProject)).thenReturn(null);
        BaseResponse baseResponse =projectService.deleteMemberProject(idAddMember,idProject,emailUser);
        assertEquals(Constant.CODE_USER_INCORRECT,baseResponse.getCode());
        assertEquals(Constant.MESS_ICORRECT_ID_PROJECT,baseResponse.getMessage());
    }

    @Test
    void testDeleteMemberProjectUserEmailNull(){
        Long idAddMember=1L;
        Long idProject=1L;
        String emailUser="usuario@example.com";
        when(projectUserRepository.findByLeaderId(idAddMember)).thenReturn(projectUser);
        when(projectUserRepository.findByEmail(emailUser)).thenReturn(null);
        when(projectRepository.findByProjectId(idProject)).thenReturn(project);
        BaseResponse baseResponse =projectService.deleteMemberProject(idAddMember,idProject,emailUser);
        assertEquals(Constant.CODE_EMAIL_NOT_EXIST,baseResponse.getCode());
        assertEquals(Constant.MESS_EMAIL_NOT_EXIST,baseResponse.getMessage());
    }
    @Test
    void testDeleteMemberProjectUserError(){
        Long idAddMember=1L;
        Long idProject=1L;
        String emailUser="usuario@example.com";
        when(projectUserRepository.findByLeaderId(idAddMember)).thenReturn(projectUser);
        when(projectUserRepository.findByEmail(emailUser)).thenReturn(projectUser);
        when(projectRepository.findByProjectId(idProject)).thenReturn(project);
        when(projectService.deleteMemberProject(idAddMember,idProject,emailUser)).thenThrow();
        BaseResponse baseResponse =projectService.addMemberProject(idAddMember,idProject,emailUser);
        assertEquals(Constant.CODE_ERROR,baseResponse.getCode());
        assertEquals(Constant.MESS_ERROR,baseResponse.getMessage());
    }
}


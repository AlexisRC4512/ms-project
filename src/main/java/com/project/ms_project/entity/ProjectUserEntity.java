package com.project.ms_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project_user")
public class ProjectUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_user_id")
    private Long projectUserId;

    @Column(name = "Role_user", nullable = false, length = 150)
    private String roleUser;

    @Column(name = "State", nullable = false)
    private Integer state;

    @Column(name = "leader_id")
    private Long leaderId;

    @Column(name = "Member_ID")
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "Project_ID", nullable = false)
    private ProjectEntity project;

    @Column(name = "Email", nullable = false, length = 150)
    private String email;

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @Column(name = "Created_by", length = 150)
    private String createdBy;

    @Column(name = "Date_created")
    private Timestamp dateCreated;

    @Column(name = "Modified_by", length = 150)
    private String modifiedBy;

    @Column(name = "Date_modified")
    private Timestamp dateModified;

    @Column(name = "Deleted_by", length = 150)
    private String deletedBy;

    @Column(name = "Date_deleted")
    private Timestamp dateDeleted;
}

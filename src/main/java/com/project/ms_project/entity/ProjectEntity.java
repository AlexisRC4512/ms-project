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
@Table(name = "project")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Project_ID")
    private Long projectId;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Description")
    private String description;

    @Column(name = "Start_Date", nullable = false)
    private Timestamp startDate;

    @Column(name = "Delivery_Date", nullable = false)
    private Timestamp deliveryDate;

    @Column(name = "State", nullable = false)
    private Integer state;

    @Column(name = "Percentage_complete", nullable = false)
    private Integer percentageComplete;

    @Column(name = "Project_created_by", length = 45,nullable = true)
    private String projectCreatedBy;

    @Column(name = "Date_created",nullable = true)
    private Timestamp dateCreated;

    @Column(name = "Project_modified_by", length = 45,nullable = true)
    private String projectModifiedBy;

    @Column(name = "Date_modified",nullable = true)
    private Timestamp dateModified;

    @Column(name = "Project_deleted_by", length = 45,nullable = true)
    private String projectDeletedBy;

    @Column(name = "Date_deleted",nullable = true)
    private Timestamp dateDeleted;
}

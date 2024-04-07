package com.project.ms_project.aggregates.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class UpdateProjectRequest {
    private String name;
    private String description;
    private Timestamp startDate;
    private Timestamp deliveryDate;
    private Integer state;
    private Integer percentageComplete;
}

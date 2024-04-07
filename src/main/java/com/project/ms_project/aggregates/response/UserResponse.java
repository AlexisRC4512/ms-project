package com.project.ms_project.aggregates.response;

import com.project.ms_project.aggregates.util.Data;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponse {
    private int code;
    private String message;
    private Data data;
}

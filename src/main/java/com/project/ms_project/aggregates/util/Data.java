package com.project.ms_project.aggregates.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Builder
public class Data {
    private Long id;
    private String numDoc;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private List<String> roleUser;
}

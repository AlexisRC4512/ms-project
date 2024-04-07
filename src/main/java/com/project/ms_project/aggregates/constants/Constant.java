package com.project.ms_project.aggregates.constants;

public class Constant {
    public static final Integer CODE_SUCCESS=200;
    public static final Integer CODE_ERROR=404;
    public static final Integer CODE_USER_INCORRECT =402;
    public static final String MESS_SUCCESS="correct execution";
    public static final String MESS_USER_INCORRECT="User ID is incorrect or does not exist";
    public static final String MESS_ERROR="execution error";
    public static  final String MESS_ICORRECT_ID_PROJECT="The project id is incorrect or does not exist";
    public static final String MESS_ADVANCE_INCORRECT="The advance cannot be greater than 100 or less than 0";
    public static final Integer CODE_ERROR_ADVANCE_INCORRECT=409;
    public static final String MESS_USER_NO_LEADER="user not leader";
    public static final Integer CODE_USER_NO_LEADER=406;
    public static final Integer CODE_EMAIL_NOT_EXIST=407;
    public static final Integer CODE_NOT_AUTORIZED=401;
    public static final String MESS_NOT_AUTORIZED="NOT AUTORIZED";
    public static final String MESS_EMAIL_NOT_EXIST= "The email is incorrect or does not exist.";
}

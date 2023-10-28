package com.class302.omzteam.organization.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserCommand {
    private int mem_no;
    private String mem_name;
    private int dept_no;
    private int job_no;
    private String job;
    private String birth;
    private String email;
    private String phone;
    private String hiredate;
    private String dept;

}

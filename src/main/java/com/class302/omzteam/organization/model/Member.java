package com.class302.omzteam.organization.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Member {
    private int mem_no;
    private String mem_pw;
    private String mem_name;
    private int job;
    private String birth;
    private String email;
    private String phone;
    private int dept_no;
    private Date hiredate;

}

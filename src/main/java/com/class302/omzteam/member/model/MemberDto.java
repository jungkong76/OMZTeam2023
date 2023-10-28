package com.class302.omzteam.member.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Builder
public class MemberDto {
    private Long mem_no;
    private String mem_pw;
    private String mem_name;
    private Integer job;
    private String birth;
    private String email;
    private String phone;
    private Long dept_no;
    private Date hiredate;// 로그인 할 때 ResultSetSpy 문제 됨 Date 타입으로 변경
    private String dept;

    public boolean is_initial_login = true;

    public String getUsername() {
        return mem_no.toString(); // 아이디는 mem_no로 설정
    }


}

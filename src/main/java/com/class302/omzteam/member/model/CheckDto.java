package com.class302.omzteam.member.model;

import lombok.*;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CheckDto {
    private Long dw_id;
    private Long member_no;
    private Time check_in;
    private Time check_out;
    private Time work_hours;
    private String date_id;
    private Long is_late;
    private String overtime;
}

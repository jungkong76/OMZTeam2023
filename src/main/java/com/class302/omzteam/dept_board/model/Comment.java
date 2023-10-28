package com.class302.omzteam.dept_board.model;

import lombok.*;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Comment {
    private int comment_no;
    private int mem_no;
    private String mem_name;
    private  String job;
    private String comment;
    private Time regdate;
    private Time updatedate;
    private int bod_no;

}

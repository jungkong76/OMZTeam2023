package com.class302.omzteam.dept_board.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Dept_board {
    private int bod_no;
    private int dept_no;
    private int mem_no;
    private String title;
    private String writer;
    private String  content;
    private String regdate;
    private String updatedate;
    private int is_notice;

}

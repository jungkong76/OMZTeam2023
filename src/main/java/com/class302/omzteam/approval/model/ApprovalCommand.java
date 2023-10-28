package com.class302.omzteam.approval.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalCommand {
    //글 번호
    private int board_id;
    //통과 여부. 기본값 '대기', 확인 후 '통과', '반려' 둘중 하나로 변경
    private String pass;
    //소속 부서
    private String dept_no;
    //작성자 이름
    private String member_name;
    //작성자 사원 번호
    private int member_no;
    //직책 번호
    private String job1;
    //결재자 이름
    private String member_name2;
    //결재자 사원 번호
    private int member_no2;
    //직책 번호
    private String job2;
    //작성 시간
    private Date regdate;
    //결재 완료 시간
    private Date comdate;
    //글 제목
    private String title;
    //글 본문
    private String content;
    //결재자 코멘트
    private String comment;

}
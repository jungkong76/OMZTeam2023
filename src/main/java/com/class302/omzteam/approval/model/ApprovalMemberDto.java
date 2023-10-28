package com.class302.omzteam.approval.model;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Builder
public class ApprovalMemberDto {

    private Integer job;

    private Integer dept_no;

    private Integer mem_no;
}

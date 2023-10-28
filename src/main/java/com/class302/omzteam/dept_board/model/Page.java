package com.class302.omzteam.dept_board.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class Page {

    private int pageNo; //현재 페이지
    private int pagsSize = 10; //한화면에 보이는 글 갯수 10개
    private int totalCount; //총 글수 수

    private int startNo; //이번 페이지의 시작  글번호
    private int endNo; //이번페이지의 끝 끝 글번호.

    private int startPage; //시작페이지
    private int endPage; //끝 페이지
    private int totalPage; //총 페이지 수

    public Page(int pageNo, int totalCount) {// totalCount =conut
        this.totalCount = totalCount;

        totalPage = (totalCount - 1) / pagsSize + 1;

        this.pageNo = (pageNo < 1) ? 1 : pageNo;
        this.pageNo = (pageNo > totalPage) ? totalPage : pageNo;

        startNo = (this.pageNo - 1) * pagsSize + 1;
        endNo = startNo + (pagsSize - 1);

        this.endNo = this.endNo > totalCount ? totalCount : this.endNo;

        startPage = (this.pageNo - 1) / 10 * 10 + 1;

        endPage = startPage + 9;

        this.endPage = this.endPage > totalPage ? totalPage : this.endPage;


    }


}

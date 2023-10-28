package com.class302.omzteam.mybatis;


import com.class302.omzteam.approval.model.ApprovalDto;
import com.class302.omzteam.approval.model.ApprovalMemberDto;
import com.class302.omzteam.approval.model.ApprovalSubDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ApprovalDao {

    //게시글 수
    @Select("select count(*) from approval")
    int count();

    //페이징
    @Select("SELECT board_id, dept_no, job1, job2, pass, member_name, member_name2, regdate, comdate, title FROM approval ORDER BY board_id DESC LIMIT #{pageSize} OFFSET #{offset}")
    List<ApprovalDto> findApprovalWithPaging(@Param("offset") int offset, @Param("pageSize") int pageSize);

    //한명정보 불러오기
    @Select("SELECT board_id, job1, dept_no, member_name, title, content, comment, pass, member_no FROM approval where board_id = #{board_id}")
    ApprovalDto selectOne(int board_id);

    @Select("SELECT job, dept_no, mem_no FROM member where mem_no = #{member_no}")
    ApprovalMemberDto selectTwo(int member_no);

    @Select("SELECT * FROM approval where dept_no= #{dept_no} ORDER BY board_id DESC;")
    List<ApprovalDto> findAll2(int dept_no);

    @Select("SELECT * FROM approval where pass = #{value} ORDER BY board_id DESC;")
    List<ApprovalDto> valueAll(String value);

    @Select("SELECT * FROM approval where dept_no= #{dept_no} and pass = #{value} ORDER BY board_id DESC;")
    List<ApprovalDto> valueDept(int dept_no,String value);

    @Select("SELECT count(*) FROM approval where dept_no= #{dept_no} and  pass = '대기' ORDER BY board_id DESC;")
    int approvalCount(int dept_no);

    //게시판 표시
    @Select("SELECT board_id, dept_no, job1, job2, pass, member_name, member_name2, regdate, comdate, title FROM approval order by board_id desc")
    List<ApprovalDto> findAll();

    //결재 등록. pass(통과여부에 '대기' 입력해줌)
    @Insert("INSERT INTO approval (member_name, member_no, dept_no, job1, pass, regdate, title, content) VALUES (#{member_name}, #{member_no}, #{dept_no}, #{job1}, '대기', NOW(), #{title}, #{content})")
    void insert1(ApprovalDto dto);

    //통과시 업데이트
    @Update("UPDATE approval SET member_name2 = #{member_name2}, member_no2 = #{member_no2}, job2 = #{job2}, pass = '통과', comdate = now(), comment = #{comment} WHERE board_id = #{board_id}")
    int updateOne(ApprovalSubDto subDto);

    //반려시 업데이트
    @Update("UPDATE approval SET member_name2 = #{member_name2}, member_no2 = #{member_no2}, job2 = #{job2}, pass = '반려', comdate = now(), comment = #{comment} WHERE board_id = #{board_id}")
    int updateTwo(ApprovalSubDto subDto);

    @Delete("DELETE FROM approval WHERE board_id = #{board_id} ")
    void deleteOne(int board_id);
}

package com.class302.omzteam.mybatis;


import com.class302.omzteam.dept_board.model.Comment;
import com.class302.omzteam.dept_board.model.Dept_board;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDao {




    @Insert("insert into comment (mem_no, mem_name, comment, job,regdate, bod_no) VALUES (#{mem_no},#{mem_name},#{comment}, #{job},now(),#{bod_no})")
    void commentinsert(Comment comment);


    @Select("select * FROM comment WHERE bod_no = #{bod_no}")
    List<Comment> commentAll(int bod_no);

    @Update("update comment set comment = #{comment},updatedate=now(),is_notice = #{is_notice} WHERE bod_no = #{bod_no}")
    void updateDb(Dept_board deptBoard);

    @Delete("DELETE FROM comment WHERE comment_no = #{comment_no}")
    void deleteComment(int comment_no);

}


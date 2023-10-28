package com.class302.omzteam.mybatis;


import com.class302.omzteam.dept_board.model.Dept_board;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface Dept_boardDao {


    @Select("SELECT COUNT(*) FROM dept_board where dept_no=#{dept_no}")
    int deptBoardCount(int dept_no);

    @Select("SELECT COUNT(*) FROM dept_board where dept_no=#{dept_no} AND writer LIKE CONCAT('%', #{value}, '%')")
    int nameCount(int dept_no, String value);

    @Select("SELECT COUNT(*) FROM dept_board where dept_no=#{dept_no} AND title LIKE CONCAT('%', #{value}, '%')")
    int titleCount(int dept_no, String value);

    @Select("SELECT * FROM dept_board where dept_no = #{dept_no} ORDER BY is_notice desc,updatedate desc LIMIT #{page}, 10;")
    List<Dept_board> lists(int dept_no, int page);

    @Select("select * FROM dept_board WHERE dept_no = #{dept_no} and is_notice = 0 ORDER BY regdate desc;")
    List<Dept_board> dbpard0(int dept_no);

    @Select("select * FROM dept_board WHERE dept_no = #{dept_no} and is_notice = 1 ORDER BY regdate desc;")
    List<Dept_board> dbpard1(int dept_no);

    @Select("select * FROM dept_board WHERE bod_no = #{bod_no}")
    Dept_board boardOne(int bod_no);

    @Select("select * FROM dept_board WHERE dept_no = #{dept_no} AND writer LIKE CONCAT('%', #{value}, '%') ORDER BY is_notice desc,updatedate desc LIMIT #{page}, 10")
    List<Dept_board> nameselect(int dept_no, String value,int page);

    @Select("select * FROM dept_board WHERE dept_no = #{dept_no} AND title LIKE CONCAT('%', #{value}, '%') ORDER BY is_notice desc,updatedate desc LIMIT #{page}, 10")
    List<Dept_board> titleselect(int dept_no,String value,int page);




    @Update("update dept_board set title = #{title},content =#{content}, updatedate=now(),is_notice = #{is_notice} WHERE bod_no = #{bod_no}")
    void updateDb(Dept_board deptBoard);

    @Insert("INSERT INTO dept_board(dept_no, mem_no, title, writer, content, regdate, is_notice,updatedate) VALUES (#{dept_no}, #{mem_no}, #{title}, #{writer},#{content},NOW(), #{is_notice},now())")
    void insertDb(Dept_board dbd);

    @Delete("DELETE FROM dept_board WHERE bod_no = #{bod_no}")
    void  deleteDb(int bod_no);



}


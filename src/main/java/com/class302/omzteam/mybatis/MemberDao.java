package com.class302.omzteam.mybatis;

import com.class302.omzteam.event.model.AllEventsDto;
import com.class302.omzteam.member.model.CheckDto;
import com.class302.omzteam.member.model.MemberDto;
import com.class302.omzteam.organization.model.Member;
import org.apache.ibatis.annotations.*;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MemberDao {

    @Select("select m.mem_name, d.dept from member m, dept d where mem_no = #{username} and m.dept_no = d.dept_no")
    MemberDto selectOneMem(String username);


    @Select("select * from daily_workstatus " +
            "where member_no = #{no}")
    List<CheckDto> myCheckList(@Param("no") Long no);


    // 출, 퇴근 관련
    @Select("select date_id from dates where event_date = #{date}")
    Integer getEventDateId(String no, LocalDate date);

    @Insert("INSERT INTO dates (event_date) VALUES (#{date})")
    int insertEventDateId(LocalDate date);

    @Insert("INSERT INTO daily_workstatus " +
            "(member_no,check_in,date_id,is_late) " +
            "VALUES " +
            "(#{no}, DATE_FORMAT(NOW(), '%H:%i:%s'), #{dateId}, 0)")
    int insertCheckIn(String no, int dateId);

    @Select("select count(check_in) from daily_workstatus where member_no = #{no} and date_id = #{dateId}")
    int hasCheckedIn(String no, int dateId);

    @Update("UPDATE daily_workstatus " +
            "SET check_out = DATE_FORMAT(NOW(), '%H:%i:%s') " +
            "WHERE member_no = #{no} and date_id = #{dateId}")
    int updateCheckOut(String no, int dateId);

    @Select("select count(check_out) from daily_workstatus where member_no = #{no} and date_id = #{dateId}")
    int hasCheckedOut(String no, int dateId);

    @Select("select check_in from daily_workstatus " +
            "where member_no = #{no} and date_Id = #{dateId}")
    Time selectByInTime(String no, int dateId);

    @Select("select check_out from daily_workstatus " +
            "where member_no = #{no} and date_id = #{dateId}")
    Time selectByOutTime(String no, int dateId);

    @Select("select * FROM MEMBER WHERE mem_no = #{mem_no}")
    Member memberByno(int mem_no);

    @Select("select * FROM MEMBER WHERE dept_no = #{dept_no}")
    List<Member> memberBydept(int dept_no);

    @Select("select * FROM MEMBER ORDER BY  job asc")
    List<Member> memberAll();

    @Select("SELECT dw.check_in AS checkin_time, dw.check_out AS checkout_time, d. event_date AS date from daily_workstatus dw " +
            "LEFT JOIN dates d ON dw.date_id = d.date_id where dw.member_no = ${no}")
    List<AllEventsDto> getCheckInout(String no);

}

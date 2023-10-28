package com.class302.omzteam.mybatis;

import com.class302.omzteam.event.model.AllEventsDto;
import com.class302.omzteam.event.model.DateModel;
import com.class302.omzteam.event.model.EventModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EventDao {

    @Insert("INSERT INTO dates (event_date) VALUES (#{eventDate, jdbcType=DATE})")
    void insertDate(@Param("eventDate") String eventDate);

    @Select("SELECT MAX(date_id) AS last_date_id FROM dates")
    long lastInsertDateId();

    @Select("Select * from dates where event_date = #{eventDate} ")
    DateModel SearchDate(@Param("eventDate") String eventDate);

    @Insert("INSERT INTO events (date_id, member_no, event_title, event_description) VALUES (#{dateId},#{memberId} ,#{title}, #{description})")
    void insertDescription(@Param("dateId") long dateId,
                           @Param("memberId") String memberId,
                           @Param("title") String title,
                           @Param("description") String description);
    @Select("SELECT\n" +
            "    d.event_date AS date,\n" +
            "    e.event_title AS eventtitle,\n" +
            "    e.event_description AS eventdescription\n" +
            "FROM events e\n" +
            "LEFT JOIN dates d ON e.date_id = d.date_id\n" +
            "WHERE e.event_id = #{eventId}; ")
    EventModel SearchEvent(@Param("eventId") int eventId);
    @Select("SELECT\n" +
            "    e.member_no AS member_id,\n" +
            "    d.event_date AS date,\n" +
            "    e.event_title AS event_title,\n" +
            "    d.date_id AS url," +
            "    e.event_id AS event_id\n" +
            "FROM events e\n" +
            "LEFT JOIN dates d ON e.date_id = d.date_id\n" +
            "WHERE e.member_no = #{no}\n")
    List<AllEventsDto> getEventsList(String no);

    @Update("update events set event_title = #{event_title}, event_description = #{event_description} where event_id = #{event_id}")
    void updateEvent(
            @Param("event_id")int id,
            @Param("event_title")String title,
            @Param("event_description")String description);

    @Delete("delete from events where event_id = #{event_id}")
    int delete(@Param("event_id")int id);

}
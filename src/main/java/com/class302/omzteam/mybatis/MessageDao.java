package com.class302.omzteam.mybatis;



import com.class302.omzteam.Message.dto.FileDto;
import com.class302.omzteam.Message.dto.Message;
import com.class302.omzteam.Message.dto.MessageMem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageDao {

    List<Message> main(int listener, int startRow, int endRow);

    Message read(int mno);

    void checked(int mno, int listener);

    void send(int sender, int listener, String mTitle, String mContent);

    @Select("SELECT subquery.mno, subquery.sender, subquery.sender_name, subquery.listener, subquery.listener_name, subquery.listener_dept,\n" +
            "    subquery.mtitle, subquery.mcontent, subquery.date_sent,subquery.date_read, subquery.READ_OR_NOT, subquery.fileOrNot\n" +
            "FROM ( SELECT s.mno, s.sender, (SELECT mem_name FROM member WHERE mem_no = s.sender) AS sender_name, s.listener,\n" +
            "(SELECT mem_name FROM member WHERE mem_no = s.listener) AS listener_name,\n" +
            "(SELECT dept FROM dept WHERE dept_no = (SELECT dept_no FROM member WHERE mem_no = s.listener)) AS listener_dept,\n" +
            "s.mtitle, s.mcontent, date_format(s.date_sent, '%y.%m.%d %H:%i') as date_sent, s.READ_OR_NOT, date_format(s.date_read, '%y.%m.%d %H:%i') as date_read,\n" +
            "(select exists (select * from files where mno = s.mno)) as fileOrNot, ROW_NUMBER() OVER (ORDER BY s.DATE_SENT DESC) as row_num FROM message s WHERE s.SENDER = #{sender}\n" +
            "AND s.mno NOT IN (SELECT mno FROM message_status WHERE DELETED_BY = #{sender}) \n" +
            ") subquery\n" +
            "WHERE subquery.row_num BETWEEN #{startRow} AND #{endRow};")
    List<Message> my_sent(int sender, int startRow, int endRow);

    @Select("SELECT count(*) FROM message WHERE SENDER = #{sender} AND mno NOT IN (SELECT mno FROM message_status WHERE DELETED_BY = #{sender})")
    int countMySent(int sender);

    @Select("SELECT subquery.mno, subquery.sender, subquery.sender_name, subquery.listener, subquery.listener_name, subquery.sender_dept,\n" +
            "subquery.mtitle, subquery.mcontent, subquery.date_sent, subquery.READ_OR_NOT, subquery.fileOrNot \n" +
            " FROM ( SELECT s.mno, s.sender, (SELECT mem_name FROM member WHERE mem_no = s.sender) AS sender_name, s.listener,\n" +
            "(SELECT mem_name FROM member WHERE mem_no = s.listener) AS listener_name,\n" +
            "(SELECT dept FROM dept WHERE dept_no = (SELECT dept_no FROM member WHERE mem_no = s.sender)) AS sender_dept,\n" +
            "s.mtitle, s.mcontent, date_format(s.date_sent, '%y.%m.%d %H:%i') as date_sent, s.read_or_not,\n" +
            "(select exists (select * from files where mno = s.mno)) as fileOrNot, ROW_NUMBER() OVER (ORDER BY s.DATE_SENT DESC) as row_num\n" +
            "FROM message s WHERE s.listener = #{listener} AND s.mno NOT IN (SELECT mno FROM message_status WHERE DELETED_BY = #{listener})\n" +
            ") subquery\n" +
            "WHERE subquery.row_num BETWEEN #{startRow} AND #{endRow};")
    List<Message> my_received(int listener, int startRow, int endRow);

    @Select("SELECT count(mno) FROM message WHERE listener = #{listener} AND mno NOT IN (SELECT mno FROM message_status WHERE DELETED_BY = #{listener})")
    int countMyRec(int listener);

    @Update("INSERT INTO MESSAGE_STATUS(MNO, DELETED_BY) VALUES(#{mno}, #{mem_no});")
    void deleteMsg(int mno, int mem_no);

    @Delete("DELETE FROM MESSAGE_STATUS WHERE MNO = #{mno} AND DELETED_BY = #{mem_no}")
    void reviveMsg(int mno, int mem_no);

    @Select("SELECT subquery.mno, subquery.sender, subquery.sender_dept, subquery.sender_name, subquery.listener, subquery.listener_dept, subquery.listener_name,\n" +
            "subquery.mtitle, subquery.mcontent, subquery.date_sent, subquery.fileOrNot\n" +
            "FROM (SELECT s.mno AS mno, s.sender, (SELECT dept FROM dept WHERE dept_no = (SELECT dept_no FROM member WHERE mem_no = s.sender)) AS sender_dept,\n" +
            "(SELECT mem_name FROM member WHERE mem_no = s.sender) AS sender_name,\n" +
            "s.listener,  (SELECT dept FROM dept WHERE dept_no = (SELECT dept_no FROM member WHERE mem_no = s.listener)) AS listener_dept,\n" +
            "(SELECT mem_name FROM member WHERE mem_no = s.listener) AS listener_name,  s.mtitle, s.mcontent, date_format(s.date_sent, '%y.%m.%d %H:%i') as date_sent,\n" +
            "(select exists (select * from files where mno = s.mno)) as fileOrNot, ROW_NUMBER() OVER (ORDER BY s.DATE_SENT DESC) as row_num\n" +
            "FROM message s WHERE s.mno IN (SELECT mno FROM message_status WHERE DELETED_BY = #{mem_no})\n" +
            "AND s.mno NOT IN (SELECT mno FROM message_status WHERE IS_DELETED_BY = #{mem_no})\n" +
            ") subquery\n" +
            "where subquery.row_num between #{startRow} and #{endRow};")
    List<Message> showBin(int mem_no, int startRow, int endRow);

    @Select("SELECT count(mno) FROM message WHERE mno IN (SELECT mno FROM message_status WHERE DELETED_BY = #{mem_no}) AND mno NOT IN (SELECT mno FROM message_status WHERE IS_DELETED_BY = #{mem_no})")
    int countMyBin(int mem_no);
    @Update("update message_status set is_deleted_by = #{mem_no} where mno = #{mno}")
    void deleteMsgForever(int mno, int mem_no);

    @Delete("update message_status set is_deleted_by = #{mem_no} where deleted_by = #{mem_no}") //휴지통에 있는 모든 쪽지 삭제.
    void deleteAllMsgForever(int mem_no);

    @Select("SELECT m.mem_no, concat(m.mem_name, ' (', d.dept, ')') as name from member m, dept d where d.dept_no = m.dept_no")
    List<MessageMem> searchMem();

    @Select("SELECT COUNT(m.mno) FROM message m LEFT JOIN message_status s ON m.mno = s.mno WHERE m.listener = #{listener}\n" +
            "AND m.read_or_not = 0 AND (s.mno IS NULL OR not s.deleted_by = #{listener});")
    int countUnread(int listener);

    @Insert("insert into files(mno, ogFile_name, file_name, file_path, file_size, created_date) values (#{mno}, #{ogFile_name}, #{file_name}, #{file_path}, #{file_size}, now())")
    void uploadFile(FileDto file);

    @Select("SELECT FNO, OGFILE_NAME, FILE_NAME, FILE_PATH, round((FILE_SIZE /1024)) as file_size FROM FILES WHERE MNO = #{mno}")
    List<FileDto> selectMessageFiles(int mno);

    @Select("SELECT fno, OGFILE_NAME, FILE_NAME, FILE_PATH, FILE_SIZE FROM FILES WHERE fNO = #{fno}")
    FileDto selectOneFile(int fno);

    @Select("SELECT max(mno) from message")
    int lastInsertedMno();

    @Update("UPDATE message m LEFT JOIN message_status s ON m.mno = s.mno\n" +
            "SET m.read_or_not = 1, date_read= now() WHERE m.listener = #{listener} AND m.read_or_not = 0 AND (s.mno IS NULL OR s.deleted_by != #{listener});")
    void readAll(int listener);

    @Select("SELECT subquery.mno, subquery.sender, subquery.sender_name, subquery.listener, subquery.listener_name, subquery.sender_dept,\n" +
            "subquery.mtitle, subquery.mcontent, subquery.date_sent, subquery.READ_OR_NOT, subquery.fileOrNot \n" +
            " FROM ( SELECT s.mno, s.sender, (SELECT mem_name FROM member WHERE mem_no = s.sender) AS sender_name, s.listener,\n" +
            "(SELECT mem_name FROM member WHERE mem_no = s.listener) AS listener_name,\n" +
            "(SELECT dept FROM dept WHERE dept_no = (SELECT dept_no FROM member WHERE mem_no = s.sender)) AS sender_dept,\n" +
            "s.mtitle, s.mcontent, date_format(s.date_sent, '%y.%m.%d %H:%i') as date_sent, s.read_or_not,\n" +
            "(select exists (select * from files where mno = s.mno)) as fileOrNot, ROW_NUMBER() OVER (ORDER BY s.DATE_SENT DESC) as row_num\n" +
            "FROM message s WHERE s.listener = #{listener} AND s.mno NOT IN (SELECT mno FROM message_status WHERE DELETED_BY = #{listener}) and s.sender = #{sender}\n" +
            ") subquery\n" +
            "WHERE subquery.row_num BETWEEN #{startRow} AND #{endRow};")
    List<Message> my_recBySO(int listener, int startRow, int endRow, String sender);

    @Select("SELECT subquery.mno, subquery.sender, subquery.sender_name, subquery.listener, subquery.listener_name, subquery.listener_dept,\n" +
            "    subquery.mtitle, subquery.mcontent, subquery.date_sent,subquery.date_read, subquery.READ_OR_NOT, subquery.fileOrNot\n" +
            "FROM ( SELECT s.mno, s.sender, (SELECT mem_name FROM member WHERE mem_no = s.sender) AS sender_name, s.listener,\n" +
            "(SELECT mem_name FROM member WHERE mem_no = s.listener) AS listener_name,\n" +
            "(SELECT dept FROM dept WHERE dept_no = (SELECT dept_no FROM member WHERE mem_no = s.listener)) AS listener_dept,\n" +
            "s.mtitle, s.mcontent, date_format(s.date_sent, '%y.%m.%d %H:%i') as date_sent, s.READ_OR_NOT, date_format(s.date_read, '%y.%m.%d %H:%i') as date_read,\n" +
            "(select exists (select * from files where mno = s.mno)) as fileOrNot, ROW_NUMBER() OVER (ORDER BY s.DATE_SENT DESC) as row_num FROM message s WHERE s.SENDER = #{sender}\n" +
            "AND s.mno NOT IN (SELECT mno FROM message_status WHERE DELETED_BY = #{sender}) and s.listener = #{listener} \n" +
            ") subquery\n" +
            "WHERE subquery.row_num BETWEEN #{startRow} AND #{endRow};")
    List<Message> my_sentBySO(int sender, int startRow, int endRow, String listener);

    @Select("SELECT count(*) FROM message s WHERE s.listener = #{listener} AND s.mno NOT IN (SELECT mno FROM message_status WHERE DELETED_BY = #{listener}) and s.sender = #{sender}")
    int countSelMyRec(int listener, String sender);

    @Select("SELECT count(*) FROM message s WHERE s.sender = #{sender} AND s.mno NOT IN (SELECT mno FROM message_status WHERE DELETED_BY = #{sender}) and s.listener = #{listener}")
    int countSelMySent(String sender, int listener);
}

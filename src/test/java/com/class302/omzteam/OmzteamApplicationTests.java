package com.class302.omzteam;

import com.class302.omzteam.event.service.EventSerivce;
import com.class302.omzteam.member.model.CheckDto;
import com.class302.omzteam.member.model.MemberDto;
import com.class302.omzteam.mybatis.MemberDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class OmzteamApplicationTests {

    @Autowired
    MemberDao memberDao;

    @Autowired
    EventSerivce eventSerivce;

    @Test
    void contextLoads() {
    }

//    @Test
//    public void getDateTime() {
//        Time dt = memberDao.selectByInTime(111L);
//        System.out.println(dt);
//    }
//
//    @Test
//    public void getDateId() {
//        LocalDate nowDate = LocalDate.of(2023, 10, 7);
//        Integer rowCnt = memberDao.getEventDateId(, nowDate);
//        System.out.println("rowCnt: " + rowCnt);
//    }
//
//    @Test
//    public void insertDateId() {
//        LocalDate nowDate = LocalDate.now();
//        int rowCnt = memberDao.insertEventDateId(nowDate);
//        System.out.println("rowCnt: " + rowCnt);
//    }
//
//    @Test
//    public void eventMain() {
//        Long no = 111L;
//        List<CheckDto> checkDtos = memberDao.myCheckList(111L);
//        System.out.println(checkDtos);
//
//    }
//
//    @Test
//    public void testMethod() {
//
//        CheckDto checkDto = new CheckDto();
//            checkDto.setDate_id("2023-09-26");
//            checkDto.setCheck_in(new Time(8,22,35));
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//
//        String formattedDateTime = dateFormat.format(new Date(checkDto.getCheck_in().getTime()));
//
//        System.out.println("날짜 및 시간: "+formattedDateTime);
//    }
//
//    @Test
//    public void testMethod2() {
//        CheckDto checkDto = new CheckDto();
//        checkDto.setDate_id("2023-09-26");
//        checkDto.setCheck_in(new Time(8, 22, 35));
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
//        LocalDateTime dateTime = LocalDateTime.of(2023, 9, 26, checkDto.getCheck_in().getHours(),
//                checkDto.getCheck_in().getMinutes(),
//                checkDto.getCheck_in().getSeconds());
//        String formattedDateTime = dateTime.format(formatter);
//
//        System.out.println("날짜 및 시간: " + formattedDateTime);
//    }
}

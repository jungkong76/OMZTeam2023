package com.class302.omzteam.member.service;

import com.class302.omzteam.event.model.AllEventsDto;
import com.class302.omzteam.mybatis.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@Service
public class CheckService {

    @Autowired
    MemberDao memberDao;

    // check in 관련
    public int getDateId(String no) {
        LocalDate nowDate = LocalDate.now();
        Integer rowCnt = memberDao.getEventDateId(no, nowDate);
        return rowCnt == null ? 0 : rowCnt;
    }
    public int insertDateId() {
        LocalDate nowDate = LocalDate.now();
        int rowCnt = memberDao.insertEventDateId(nowDate);
        System.out.println("rowCnt: "+rowCnt);
        return rowCnt;
    }
    public int hasCheckedIn(String no, int dateId) {
        return memberDao.hasCheckedIn(no, dateId);
    }
    public Time getInTime(String no, int dateId) {
        return memberDao.selectByInTime(no, dateId);
    }
    public int insertCheckIn(String no, int dateId) {
        return memberDao.insertCheckIn(no, dateId);
    }

    // check out 관련
    public int hasCheckedOut(String no, int dateId) {
        return memberDao.hasCheckedOut(no, dateId);
    }
    public Time getOutTime(String no, int dateId) {
        return memberDao.selectByOutTime(no, dateId);
    }

    @Transactional
    public int updateCheckOut(String no, int dateId) {
        return memberDao.updateCheckOut(no, dateId);
    }

    public List<AllEventsDto> check(String no) {
        return memberDao.getCheckInout(no);
    }
}

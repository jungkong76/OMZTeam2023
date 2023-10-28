package com.class302.omzteam.event.service;


import com.class302.omzteam.event.model.AllEventsDto;
import com.class302.omzteam.event.model.EventModel;
import com.class302.omzteam.event.model.EventUpdateModel;
import com.class302.omzteam.mybatis.EventDao;
import com.class302.omzteam.mybatis.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service

public class EventSerivce {

    @Autowired
    EventDao eventDao;

    @Autowired
    MemberDao memberDao;

    @Transactional
    public boolean inputService(EventModel eventModel){


        if(eventModel.getDate() != null || eventModel.getEventDescription() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(eventModel.getDate());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            if(eventDao.SearchDate(formattedDate) != null){
                eventDao.insertDescription(eventDao.SearchDate(formattedDate).getDate_id(),user.getUsername(),eventModel.getEventTitle(),eventModel.getEventDescription());
            }else{
                eventDao.insertDate(formattedDate);
                long lastDateId = eventDao.lastInsertDateId();
                eventDao.insertDescription(lastDateId,user.getUsername() ,eventModel.getEventTitle(), eventModel.getEventDescription());
            }

            return true;

        }

        return false;
    }


    public List<AllEventsDto> getEvents(String no) {
        List<AllEventsDto> list = eventDao.getEventsList(no);
        System.out.println("getEventsList : "+eventDao.getEventsList(no));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");


        return eventDao.getEventsList(no);
    }
    public EventModel load(int eventId){
        System.out.println(eventId);
        return eventDao.SearchEvent(eventId);
    }
    public void updateService(EventUpdateModel updateModel){

        eventDao.updateEvent(updateModel.getEventId(), updateModel.getEventTitle(), updateModel.getEventDescription());
    }

    public boolean deleteEvent(int eventId){
    int request = eventDao.delete(eventId);
    if (request != 1){
        return false;
    }else {
        return true;
    }

    }
}

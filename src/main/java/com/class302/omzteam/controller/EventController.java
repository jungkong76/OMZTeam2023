package com.class302.omzteam.controller;


import com.class302.omzteam.event.model.AllEventsDto;
import com.class302.omzteam.event.model.EventModel;
import com.class302.omzteam.event.model.EventUpdateModel;
import com.class302.omzteam.event.service.EventSerivce;
import com.class302.omzteam.member.model.BirthDay;
import com.class302.omzteam.member.service.CheckService;
import com.class302.omzteam.mybatis.BirthDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/calendarEvent")
public class EventController {

    @Autowired
    EventSerivce eventSerivce;
    @Autowired
    CheckService checkService;

    @Autowired
    BirthDao birthDao;
//    @GetMapping("/cal")
//    public String eventMain(Model model) {
//        return "member/memberCalendar";
//    }

    @GetMapping("/birth")
    @ResponseBody
    public List<BirthDay> birthDay(){

        return birthDao.birthDay();
    }
    @GetMapping("/eventInputForm")
    public void eventInput(){
    }

    @PostMapping("/eventInputForm")
    @ResponseBody
    public boolean newEventInput(@RequestBody EventModel eventModel) {

        return eventSerivce.inputService(eventModel);
    }
    @GetMapping("/cal")
    public String eventMainForm(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        return "member/memberCalendar";
    }

    @GetMapping("/events")
    @ResponseBody
    public List<AllEventsDto> eventMain(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        return eventSerivce.getEvents(user.getUsername());
    }
    @GetMapping("/check")
    @ResponseBody
    public List<AllEventsDto> memberCheck(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        return checkService.check(user.getUsername());
    }


    @RequestMapping("/eventDeleteForm/{eventId}")
    @ResponseBody
    public boolean eventDelete(@PathVariable("eventId") int eventId){
        System.out.println(eventId+"123123123");
       return eventSerivce.deleteEvent(eventId);
    }


    @RequestMapping(value = "/event/{dateId}")
    public String eventDescription(@PathVariable("dateId") int dateId){

        return "calendarEvent/eventDesForm";
    }
    @PostMapping("/eventLoading")
    @ResponseBody
    public EventModel loading(@RequestParam("eventId") int eventId){
        return eventSerivce.load(eventId);
    }

    @PostMapping("/eventUpdateForm")
    @ResponseBody
    public void Update(@RequestBody EventUpdateModel eventModel){
        eventSerivce.updateService(eventModel);
    }

}

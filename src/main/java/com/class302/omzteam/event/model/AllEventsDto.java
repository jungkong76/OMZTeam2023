package com.class302.omzteam.event.model;

import lombok.*;

import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AllEventsDto {

    String member_id;
    private Time checkin_time;
    private Time checkout_time;
    private Date date;
    private String event_title;
    private int url;
    private int event_id;
}

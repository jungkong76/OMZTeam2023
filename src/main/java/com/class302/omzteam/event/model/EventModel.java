package com.class302.omzteam.event.model;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class EventModel {

    private Date date;
    private String eventTitle;
    private String eventDescription;
}

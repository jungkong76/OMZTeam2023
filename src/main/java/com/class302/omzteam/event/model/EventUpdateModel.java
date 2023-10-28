package com.class302.omzteam.event.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventUpdateModel {

    private int eventId;
    private String eventTitle;
    private String eventDescription;
}

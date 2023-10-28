package com.class302.omzteam.event.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventInputModel {

    private long event_id;
    private long date_id;
    private long member_id;
    private String event_title;
    private String event_description;

}

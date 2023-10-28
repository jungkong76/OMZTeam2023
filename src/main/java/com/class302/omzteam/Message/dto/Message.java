package com.class302.omzteam.Message.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
public class Message {
    private int mno;
    private int sender;
    private String sender_name;
    private String sender_dept;
    private int listener;
    private String listener_name;
    private String listener_dept;
    private String mTitle;
    private String mContent;
    private String date_sent;
    private int read_or_not;
    private String date_read;
    private int fileOrNot;

    public Message(int sender, int listener, String mTitle, String mContent) {
        this.sender = sender;
        this.listener = listener;
        this.mTitle = mTitle;
        this.mContent = mContent;
    }
}

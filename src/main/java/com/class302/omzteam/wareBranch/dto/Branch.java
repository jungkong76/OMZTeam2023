package com.class302.omzteam.wareBranch.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
public class Branch {
    private int bNum;
    private String bName;
    private String bAddress;
    private String bPhone;
    private String bAuth;
    private double lat;
    private double lng;
}


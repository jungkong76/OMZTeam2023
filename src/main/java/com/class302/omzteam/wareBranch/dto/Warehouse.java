package com.class302.omzteam.wareBranch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Warehouse {
    private int wNum;
    private String wName;
    private String wAddress;
    private String wPhone;
    private String wAuth;
    private double lat;
    private double lng;
}

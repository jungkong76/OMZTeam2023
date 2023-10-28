package com.class302.omzteam.wareBranch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class StockW {
    private String category;
    private String itemName;
    private int itemNum;
    private String wName;
    private int pStock;
    private String icDate;
    private String Manufacturer;
    private int wNum;
}

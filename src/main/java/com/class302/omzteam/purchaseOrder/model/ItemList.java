package com.class302.omzteam.purchaseOrder.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class ItemList {

    private int ItemNum;
    private String ItemName;
    private String Category;
    private String Manufacturer;
    private float price;
}

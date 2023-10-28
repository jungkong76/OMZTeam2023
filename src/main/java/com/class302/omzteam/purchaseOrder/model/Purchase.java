package com.class302.omzteam.purchaseOrder.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Purchase {

    private int PurchaseNum;
    private ItemList itemList;
    private int Quantity;
    private Date PurchaseDate;
    private Date ReceiptDate;

}

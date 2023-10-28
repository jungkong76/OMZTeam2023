package com.class302.omzteam.purchaseOrder.service;

import com.class302.omzteam.mybatis.PurchaseDao;
import com.class302.omzteam.purchaseOrder.model.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class PurchaseOrderList {

    @Autowired
    PurchaseDao purchaseDao;

    public List<Purchase> purchaseOrderList(){

        return purchaseDao.selectAllOrder();
    }

}

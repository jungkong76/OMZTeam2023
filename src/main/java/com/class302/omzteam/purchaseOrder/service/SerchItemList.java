package com.class302.omzteam.purchaseOrder.service;

import com.class302.omzteam.mybatis.PurchaseDao;
import com.class302.omzteam.purchaseOrder.model.ItemList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SerchItemList {

    @Autowired
    PurchaseDao purchaseDao;
    public List<ItemList> serchItemList(){

        return purchaseDao.GetItemList();
    }
}

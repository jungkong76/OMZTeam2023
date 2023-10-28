package com.class302.omzteam.purchaseOrder.service;

import com.class302.omzteam.mybatis.PurchaseDao;
import com.class302.omzteam.purchaseOrder.model.addOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PurchaseAdd {
    @Autowired
    PurchaseDao purchaseDao;

    public boolean puchaseAdd(addOrder addOrder){
        if(addOrder.getQuantity() == 0 || addOrder.getItemNum() == 0) {

            return false;
        }else{
            purchaseDao.insertPurchase(addOrder);
            return true;
        }
    }

}

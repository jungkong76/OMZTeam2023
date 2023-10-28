package com.class302.omzteam.mybatis;

import com.class302.omzteam.purchaseOrder.model.ItemList;
import com.class302.omzteam.purchaseOrder.model.Purchase;
import com.class302.omzteam.purchaseOrder.model.addOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PurchaseDao {

    @Select("select * from Purchaseorder")
    List<Purchase> selectAllOrder();
    @Select("select * from itemList")
    List<ItemList> GetItemList();
    @Insert("INSERT INTO purchaseorder (ItemNum, Quantity, PurchaseDate) VALUES (#{itemNum}, #{quantity}, NOW())")
    void insertPurchase(addOrder addOrder);
    @Select("select url from itemList where itemNum = #{itemNum}")
    String SelectUrl(int itemNum);
    @Select("select price from itemList where itemNum = #{itemNum}")
    Double SelectPrice(int itemNum);
}

package com.class302.omzteam.controller;

import com.class302.omzteam.purchaseOrder.model.ItemList;
import com.class302.omzteam.purchaseOrder.model.Purchase;
import com.class302.omzteam.purchaseOrder.model.ScrapedData;
import com.class302.omzteam.purchaseOrder.model.addOrder;
import com.class302.omzteam.purchaseOrder.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {
    @Autowired
    PurchaseOrderList purchaseOrderList;
    @Autowired
    SerchItemList serchItemList;
    @Autowired
    PurchaseAdd purchaseAdd;
    @Autowired
    WebScraped webScraped;
    @Autowired
    ExchangeService exchangeService;
    @GetMapping("/list")
    public void orderList(Model model){
        List<Purchase> list =  purchaseOrderList.purchaseOrderList();
        model.addAttribute("list", list);
    }
    @GetMapping("/order")
    public void order(Model model){
    List<ItemList> list = serchItemList.serchItemList();
    model.addAttribute("itemList", list);
    }
    @GetMapping("/addOrder")
    @ResponseBody
    public boolean addOrder(Model model, addOrder addOrder){
        System.out.println("addOrder: "+addOrder);
        return purchaseAdd.puchaseAdd(addOrder);
    }
    @PostMapping("/select")
    @ResponseBody
    public ScrapedData selectOption(int itemNum) throws IOException {

    return webScraped.scrapeWebsite(itemNum);
    }
    @GetMapping("/exchange")
    @ResponseBody
    public double exchangeRate() throws IOException {
        System.out.println("123");
        return exchangeService.exchangeRate();
    }
}

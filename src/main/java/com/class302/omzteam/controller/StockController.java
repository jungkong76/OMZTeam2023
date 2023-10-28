package com.class302.omzteam.controller;

import com.class302.omzteam.mybatis.BranchDao;
import com.class302.omzteam.mybatis.WarehouseDao;
import com.class302.omzteam.wareBranch.dto.Branch;
import com.class302.omzteam.wareBranch.dto.StockB;
import com.class302.omzteam.wareBranch.dto.StockW;
import com.class302.omzteam.wareBranch.dto.Warehouse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/stock")
public class StockController {

    @Autowired
    BranchDao branchDao;

    @Autowired
    WarehouseDao warehouseDao;

    @GetMapping("/list")
    public void branchList(Model model){
        List<Branch> bList = branchDao.bListAll();
        model.addAttribute("bList", bList);

        List<Warehouse> wList = warehouseDao.wListAll();
        model.addAttribute("wList", wList);
    }

    @RequestMapping( value = "/stockListB/{bNum}", method = RequestMethod.GET)
    public ModelAndView stockListByBnum(@PathVariable("bNum") int bNum, ModelAndView mav){
        mav.setViewName("Stock/stockListB");
        mav.addObject("stockList", branchDao.selectAllStockBybNum(bNum));
        mav.addObject("bName", branchDao.bSelectOne(bNum).getBName());
        return mav;
    }

    @RequestMapping( value = "/detailB/{bNum}", method = RequestMethod.GET)
    public ModelAndView branchDetail(@PathVariable("bNum") int bNum, ModelAndView mav){
        mav.setViewName("Stock/detailB");
        mav.addObject("branchDetail", branchDao.bSelectOne(bNum));
        return mav;
    }

    @RequestMapping( value = "/stockListW/{wNum}", method = RequestMethod.GET)
    public ModelAndView stockListByWnum(@PathVariable("wNum") int wNum, ModelAndView mav){
        mav.setViewName("Stock/stockListW");
        mav.addObject("stockList", warehouseDao.selectAllStockBywNum(wNum));
        mav.addObject("wName", warehouseDao.wSelectOne(wNum).getWName());
        return mav;
    }

    @RequestMapping( value = "/detailW/{wNum}", method = RequestMethod.GET)
    public ModelAndView warehouseDetail(@PathVariable("wNum") int wNum, ModelAndView mav){
        mav.setViewName("Stock/detailW");
        mav.addObject("warehouseDetail", warehouseDao.wSelectOne(wNum));
        return mav;
    }


    @PostMapping("/branch/search")
    @ResponseBody
    public Map<String, Object> stockSearchB(@RequestParam String select, @RequestParam String searchInput,
                                         @RequestParam(required = false) Integer bNum){
        Map<String, Object> map = new HashMap<>();
        List<StockB> listB = new ArrayList<>();
        if(bNum != null) {
        	listB = branchDao.searchByKeywordAndBnum(bNum, select, searchInput);
        } else {
        	listB = branchDao.searchByKeyword(select, searchInput);
        }
        if(!listB.isEmpty()) {
            map.put("success", true);
            map.put("list", listB);
        } else {
            map.put("success", false);
            map.put("list", null);
        }
        return map;
    }

    @PostMapping("/warehouse/search")
    @ResponseBody
    public Map<String, Object> stockSearchW(@RequestParam String select, @RequestParam String searchInput,
                                           @RequestParam(required = false) Integer wNum){
        Map<String, Object> map = new HashMap<>();
        List<StockW> listW = new ArrayList<>();
        if(wNum != null) {
            listW = warehouseDao.searchByKeywordAndWnum(wNum, select, searchInput);
        } else {
            listW = warehouseDao.searchByKeyword(select, searchInput);
        }
        if(!listW.isEmpty()) {
            map.put("success", true);
            map.put("list", listW);
        } else {
            map.put("success", false);
            map.put("list", null);
        }
        return map;
    }

    @PostMapping("/stock/search")
    @ResponseBody
    public Map<String, Object> stockSearch(@RequestParam String select, @RequestParam String searchInput){
        Map<String, Object> map = new HashMap<>();
        List<StockB> listB = branchDao.searchByKeyword(select, searchInput);
        List<StockW> listW = warehouseDao.searchByKeyword(select, searchInput);
        if(!listB.isEmpty()&&!listW.isEmpty()) {
            map.put("success", true);
            map.put("listB", listB);
            map.put("listW", listW);
        } else {
            map.put("success", false);
        }
        return map;
    }

    @RequestMapping( value = "branch/stockDetail/{bNum}/{itemNum}", method = RequestMethod.GET)
    public ModelAndView stockDetailB(@PathVariable("bNum") int bNum, @PathVariable("itemNum") int itemNum, ModelAndView mav){
        mav.setViewName("Stock/Branch/stockDetail");
        mav.addObject("stockDetail", branchDao.stockDetail(bNum, itemNum));
        mav.addObject("itemName", branchDao.selectItemName(itemNum));
        mav.addObject("bName", branchDao.bSelectOne(bNum).getBName());
        return mav;
    }

    @RequestMapping( value = "warehouse/stockDetail/{wNum}/{itemNum}", method = RequestMethod.GET)
    public ModelAndView stockDetailW(@PathVariable("wNum") int wNum, @PathVariable("itemNum") int itemNum, ModelAndView mav){
        mav.setViewName("Stock/Warehouse/stockDetail");
        mav.addObject("stockDetail", warehouseDao.stockDetail(wNum, itemNum));
        mav.addObject("itemName", warehouseDao.selectItemName(itemNum));
        mav.addObject("bName", warehouseDao.wSelectOne(wNum).getWName());
        return mav;
    }
}
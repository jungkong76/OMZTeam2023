package com.class302.omzteam.mybatis;

import com.class302.omzteam.wareBranch.dto.StockW;
import com.class302.omzteam.wareBranch.dto.Warehouse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WarehouseDao {

    List<Warehouse> wListAll();

    Warehouse wSelectOne(int wNum);

    String selectItemName(int itemNum);

    List<StockW> selectAllStockBywNum(int wNum);

	List<StockW> searchByKeywordAndWnum(int wNum, String select, String searchInput);

    List<StockW> searchByKeyword(String select, String searchInput);

    List<StockW> stockDetail(int wNum, int itemNum);
}

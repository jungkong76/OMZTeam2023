package com.class302.omzteam.mybatis;

import com.class302.omzteam.wareBranch.dto.Branch;
import com.class302.omzteam.wareBranch.dto.StockB;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BranchDao {

    int countBranchTest();

    List<Branch> bListAll();

    Branch bSelectOne(int bNum);
    
    String selectItemName(int itemNum);

    List<StockB> selectAllStockBybNum(int bNum);

	List<StockB> searchByKeywordAndBnum(int bNum, String select, String searchInput);
	
	List<StockB> searchByKeyword(String select, String searchInput);

	List<StockB> stockDetail(int bNum, int itemNum);
}

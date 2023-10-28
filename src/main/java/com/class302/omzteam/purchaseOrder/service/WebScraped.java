package com.class302.omzteam.purchaseOrder.service;

import com.class302.omzteam.mybatis.PurchaseDao;
import com.class302.omzteam.purchaseOrder.model.ScrapedData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

@Service
public class WebScraped {
    @Autowired
    PurchaseDao purchaseDao;
    public ScrapedData scrapeWebsite(int itemNum) throws IOException {

        // 웹 페이지 URL 지정
        String url = purchaseDao.SelectUrl(itemNum);
        Double USDresult = purchaseDao.SelectPrice(itemNum);
        // 웹 페이지 가져오기
        Document doc1 = Jsoup.connect(url).get();

        Element priceElement = doc1.selectFirst("em.prc_c");
        assert priceElement != null;
        String priceValue = priceElement.ownText();

        // 가격 문자열에서 "원" 및 쉼표(,) 제거 후 파싱
        priceValue = priceValue.replace("원", "").replace(",", "");

        return new ScrapedData(Double.parseDouble(priceValue),USDresult);
    }
}


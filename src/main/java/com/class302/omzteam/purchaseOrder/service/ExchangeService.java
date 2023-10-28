package com.class302.omzteam.purchaseOrder.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ExchangeService {

    public double exchangeRate() throws IOException {

        String urlRate = "https://finance.naver.com/marketindex/";
        Document rate = Jsoup.connect(urlRate).get();
        // 환율 정보 가져오기
        Element exchangeRateElement = rate.selectFirst("span.value");
        assert exchangeRateElement != null;
        String exchangeRateValue = exchangeRateElement.ownText();
        System.out.println("환율: " + exchangeRateValue);
        // 환율에서 쉼표(,) 제거 후 파싱
        exchangeRateValue = exchangeRateValue.replace(",", "");
        System.out.println(exchangeRateValue+"121323123");
    return Double.parseDouble(exchangeRateValue);

    }
}

package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
public class MetricServiceImpl implements MetricService {

    private final ConcurrentMap<String, Integer> cardRequestMap;

    public MetricServiceImpl(){
        cardRequestMap = new ConcurrentHashMap<>();
    }
    @Override
    public void increaseCount(String request, int status) {
        String cardNumber = extractCardNumber(request);
        cardMetric(cardNumber);
    }

    @Override
    public void cardMetric(String cardNumber) {
        cardRequestMap.putIfAbsent(cardNumber, 1);
        if(cardRequestMap.get(cardNumber) != null){
            cardRequestMap.put(cardNumber, cardRequestMap.get(cardNumber) + 1);
        }
    }

    @Override
    public String extractCardNumber(String uri) {
        log.info("extracting card number ============================>");
        String[] splitList = uri.split("/");
        log.info(Arrays.stream(splitList).count() + " number of items in the list");
        return Arrays.asList(splitList).get(splitList.length - 1);
    }

    @Override
    public Map<String, Integer> getMetric() {
        return cardRequestMap;
    }
}

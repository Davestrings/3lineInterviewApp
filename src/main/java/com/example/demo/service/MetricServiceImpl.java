package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
        String[] splitList = uri.split("/");
        return Arrays.asList(splitList).get(splitList.length - 1);
    }
}

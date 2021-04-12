package com.example.demo.service;

import java.util.Map;

public interface MetricService {
    void increaseCount(String request, int status);
    void cardMetric(String cardNumber);
    String extractCardNumber(String uri);
    Map<String, Integer> getMetric();
}

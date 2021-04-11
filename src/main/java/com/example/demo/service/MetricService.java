package com.example.demo.service;

public interface MetricService {
    public void increaseCount(String request, int status);
    public void cardMetric(String cardNumber);
    public String extractCardNumber(String uri);
}

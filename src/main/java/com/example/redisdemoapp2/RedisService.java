package com.example.redisdemoapp2;

import org.springframework.stereotype.Component;

import java.util.List;

public interface RedisService {

    String getNumberWithCacheable(int number);

    String getNumberWithCacheManager(int number);

    void saveNumbersUsingTransaction(List<Integer> numbers);

    void saveNumbersUsingTransactionWithAnnotation(List<Integer> numbers);

}

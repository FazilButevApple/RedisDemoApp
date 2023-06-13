package com.example.redisdemoapp2;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class RedisDemoApp implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisDemoApp.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RedisService redisService;

    public static void main(String[] args) {
        SpringApplication.run(RedisDemoApp.class, args);
    }


    @Override
    public void run(String... args) {
        //RedisTemplate redisTemplate = (RedisTemplate) applicationContext.getBean("redisTemplate");
        //ValueOperations valueOperations = redisTemplate.opsForValue();
        //String key = "test_key_redisTemplate_test";
        //valueOperations.set(key,"test_value");

        //LOGGER.info("RedisDemoApp2 başladı ve değerleri redise aktardı.",key);
        //redisService = applicationContext.getBean(RedisServiceImpl.class);

        //saveNumbersUsingTransaction();
        saveNumbersUsingTransactionWithAnnotation();
    }

    private void saveNumbersUsingTransaction()
    {
        List<Integer> numbers = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 5; i++)
        {
            numbers.add(r.nextInt(6));
        }
        LOGGER.info("Method: saveNumbersUsingTransaction is called with parameter: " + numbers);
        redisService.saveNumbersUsingTransaction(numbers);
    }

    private void saveNumbersUsingTransactionWithAnnotation()
    {
        List<Integer> numbers = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 5; i++)
        {
            numbers.add(r.nextInt(6));
        }
        LOGGER.info("Method: saveNumbersUsingTransaction is called with parameter: " + numbers);
        redisService.saveNumbersUsingTransactionWithAnnotation(numbers);
    }
}

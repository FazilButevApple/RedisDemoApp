package com.example.redisdemoapp2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate redisTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisDemoApp.class);


    @Override
    public String getNumberWithCacheable(int number) {
        return null;
    }

    @Override
    public String getNumberWithCacheManager(int number) {
        return null;
    }

    @Override
    public void saveNumbersUsingTransaction(List<Integer> numbers)
    {
        List<Object> txResults = (List<Object>) redisTemplate.execute(new SessionCallback()
        {
            public List<Object> execute(RedisOperations operations) throws DataAccessException
            {
                operations.multi(); // Transaction işlemi burada başlıyor.
                                    // Bir işlem bloğunun başlangıcını işaretler. Sonraki komutlar, EXEC kullanılarak atomik yürütme için kuyruğa alınacaktır.
                try
                {
                    StringBuilder redisValue = new StringBuilder("Saved numbers");
                    for (Integer number : numbers)
                    {
                        LOGGER.info("Processed number is " + number);
                        if (number.intValue() == 5)
                        {
                            throw new IllegalArgumentException("Bad number");
                        }
                        redisValue.append("___" + number);
                        operations.opsForValue().set("numbers", redisValue.toString());
                    }
                }
                catch (Exception e)
                {
                    LOGGER.error("Exception occurred: " + e.getMessage());
                    operations.discard(); // Olası bir hata durumunda transactiondan cıkılıp işlem kesiliyor.
                                        // Bir işlemde önceden kuyruğa alınmış tüm komutları temizler ve bağlantı durumunu normale döndürür.
                    return null;
                }
                return operations.exec(); // Transaction işlemi execute ediliyor.
                                        // Bir işlemde önceden kuyruğa alınan tüm komutları yürütür ve bağlantı durumunu normale döndürür.
            }
        });
        if (txResults != null)
        {
            LOGGER.info("Redis process count: " + txResults.size());
        }
    }


    @Override
    @Transactional
    public void saveNumbersUsingTransactionWithAnnotation(List<Integer> numbers)
    {
        redisTemplate.execute(new SessionCallback()
        {
            public List<Object> execute(RedisOperations operations) throws DataAccessException
            {
                operations.multi();

                StringBuilder redisValue = new StringBuilder("Saved numbers");
                for (Integer number : numbers)
                {
                    LOGGER.info("Processed number is " + number);

                    redisValue.append("___" + getNumberAsText(number));
                    operations.opsForValue().set("numbersAsText", redisValue.toString());
                }
                return null;
            }
        });
    }

    private String getNumberAsText(Integer number)
    {
        switch (number)
        {
            case 0:
                return "zero";
            case 1:
                return "one";
            case 2:
                return "two";
            case 3:
                return "three";
            case 4:
                return "four";
            case 5:
                throw new IllegalArgumentException("Bad number");
            default:
                return null;
        }
    }
}

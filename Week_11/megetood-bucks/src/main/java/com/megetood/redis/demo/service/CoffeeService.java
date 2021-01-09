package com.megetood.redis.demo.service;

import com.megetood.redis.demo.model.Coffee;
import com.megetood.redis.demo.model.CoffeeCache;
import com.megetood.redis.demo.repository.CoffeeCacheRepository;
import com.megetood.redis.demo.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

/**
 * @author Lei Chengdong
 * @date 2020/12/21
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "coffee")/*当前类使用缓存，coffee 为缓存名*/
public class CoffeeService {

    private static final String CACHE = "bucks-coffee";

    @Autowired
    private CoffeeRepository coffeeRepository;
    @Autowired
    private CoffeeCacheRepository cacheRepository;
    @Autowired
    private RedisTemplate<String, Coffee> redisTemplate;

    @Cacheable /*加此注解，当前方法查询结果将放入缓存中*/
    public List<Coffee> findAllCoffee() {
        return coffeeRepository.findAll();
    }

    @CacheEvict /*调用该方法，清理当前类的缓存*/
    public void relloadCoffee() {
    }

    public Optional<Coffee> findOneCoffee(String name) {
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", exact().ignoreCase());
        Optional<Coffee> coffee = coffeeRepository.findOne(
                Example.of(Coffee.builder().name(name).build(), matcher));

        log.info("Coffee Found: {}", coffee);
        return coffee;
    }


    public Optional<Coffee> findOneCoffeeWithRedisCache(String name) {
        HashOperations<String, String, Coffee> hashOperations = redisTemplate.opsForHash();

        // 查缓存
        if (redisTemplate.hasKey(CACHE) && hashOperations.hasKey(CACHE, name)) {
            log.info("Get coffee {} from Redis.", name);
            return Optional.of(hashOperations.get(CACHE, name));
        }

        // 查数据库
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", exact().ignoreCase());
        Optional<Coffee> coffee = coffeeRepository.findOne(
                Example.of(Coffee.builder().name(name).build(), matcher));

        log.info("Coffee Found: {}", coffee);

        // 保存到缓存
        coffee.ifPresent(c -> {
            log.info("Put coffee {} to Redis.", coffee);
            hashOperations.put(CACHE, name, coffee.get());
            redisTemplate.expire(CACHE, 1, TimeUnit.MINUTES);
        });

        return coffee;
    }

    public Optional<Coffee> findSimpleCoffeeFromCache(String name) {
        Optional<CoffeeCache> cached = cacheRepository.findOneByName(name);
        if (cached.isPresent()) {
            CoffeeCache c = cached.get();

            Coffee coffee = Coffee.builder()
                    .name(c.getName())
                    .price(c.getPrice())
                    .build();
            log.info("Coffee {} found in cache.", coffee);

            return Optional.of(coffee);
        } else {
            Optional<Coffee> raw = findOneCoffee(name);
            raw.ifPresent(c -> {
                final CoffeeCache coffeeCache = CoffeeCache.builder()
                        .name(c.getName())
                        .price(c.getPrice())
                        .build();
                log.info("Save Coffee {} to cache.", coffeeCache);

                cacheRepository.save(coffeeCache);
            });

            return raw;
        }
    }

}

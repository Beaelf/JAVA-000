package com.megetood.redis.demo;

import com.megetood.redis.demo.converter.BytesToMoneyConverter;
import com.megetood.redis.demo.converter.MoneyToBytesConverter;
import com.megetood.redis.demo.repository.CoffeeRepository;
import com.megetood.redis.demo.model.Coffee;
import com.megetood.redis.demo.service.CoffeeService;
import io.lettuce.core.ReadFrom;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * @author Lei Chengdong
 * @date 2020/12/22
 */
@Slf4j
@SpringBootApplication
@EnableRedisRepositories
@EnableCaching(proxyTargetClass = true)
public class RedisDemoApplication implements ApplicationRunner {

    @Autowired
    private CoffeeRepository coffeeRepository;
    @Autowired
    private CoffeeService coffeeService;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private JedisPoolConfig jedisPoolConfig;

    public static void main(String[] args) {
        SpringApplication.run(RedisDemoApplication.class, args);
    }

    @Bean
    @ConfigurationProperties("redis")
    public JedisPoolConfig jedisPoolConfig() {
        return new JedisPoolConfig();
    }

    @Bean(destroyMethod = "close")
    public JedisPool jedisPool(@Value("${redis.host}") String host) {
        return new JedisPool();
    }

    @Bean
    public RedisTemplate<String, Coffee> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Coffee> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    // 主从配置
    @Bean
    public LettuceClientConfigurationBuilderCustomizer customizer() {
        return builder -> builder.readFrom(ReadFrom.MASTER_PREFERRED);
    }

    @Bean
    public RedisCustomConversions redisCustomConversions() {
        return new RedisCustomConversions(
                Arrays.asList(new MoneyToBytesConverter(), new BytesToMoneyConverter()));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        jdsDemo();
        cacheDemo();
        catchWithRedisDemo();
        redisDemo();
        redisJpaDemo();
    }

    private void redisJpaDemo() {
        Optional<Coffee> coffee = coffeeService.findSimpleCoffeeFromCache("mocha");
        log.info("Coffee {}", coffee);

        for (int i = 0; i < 5; i++) {
            coffee = coffeeService.findSimpleCoffeeFromCache("mocha");
        }

        log.info("Value from Redis: {}", coffee);

    }

    private void redisDemo() {
        Optional<Coffee> latte = coffeeService.findOneCoffeeWithRedisCache("latte");
        log.info("Coffee {}", latte);

        for (int i = 0; i < 5; i++) {
            latte = coffeeService.findOneCoffeeWithRedisCache("latte");
        }
        log.info("Value from Redis: {}", latte);
    }

    private void catchWithRedisDemo() throws InterruptedException {
        log.info("Count: {}", coffeeService.findAllCoffee().size());

        for (int i = 0; i < 10; i++) {
            log.info("Reading from cache...");
            coffeeService.findAllCoffee();
        }

        Thread.sleep(5000);// 5s后 redis缓存失效，将查询数据库，打印sql

        log.info("Reading after refresh...");

        coffeeService.findAllCoffee().forEach(c -> log.info("Coffee {}", c));
    }

    private void cacheDemo() {
        log.info("Count: {}", coffeeService.findAllCoffee().size());

        for (int i = 0; i < 10; i++) {
            log.info("Reading from cache...");
            coffeeService.findAllCoffee();
        }

        coffeeService.relloadCoffee();// 刷新缓存后 redis缓存失效，将查询数据库，打印sql

        log.info("Reading after refresh...");

        coffeeService.findAllCoffee().forEach(c -> log.info("Coffee {}", c));
    }

    private void jdsDemo() {
        log.info(jedisPoolConfig.toString());

        try (Jedis jedis = jedisPool.getResource()) {
            coffeeRepository.findAll().forEach(c -> {
                jedis.hset("bucks-menu",
                        c.getName(),
                        Long.toString(c.getPrice().getAmountMinorLong()));
            });

            final Map<String, String> menu = jedis.hgetAll("bucks-menu");
            log.info("Menu: {}", menu);

            final String price = jedis.hget("bucks-menu", "latte");
            log.info("latte: {}", Money.ofMinor(CurrencyUnit.of("CNY"), Long.parseLong(price)));
        }
    }

}

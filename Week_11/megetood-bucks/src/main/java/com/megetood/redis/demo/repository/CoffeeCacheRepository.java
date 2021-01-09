package com.megetood.redis.demo.repository;

import com.megetood.redis.demo.model.CoffeeCache;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author Lei Chengdong
 * @date 2020/12/23
 */
public interface CoffeeCacheRepository extends CrudRepository<CoffeeCache,Long> {
    Optional<CoffeeCache> findOneByName(String name);
}

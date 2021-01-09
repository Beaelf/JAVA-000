package com.megetood.redis.demo.repository;

import com.megetood.redis.demo.model.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Lei Chengdong
 * @date 2020/12/21
 */
public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
}

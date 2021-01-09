package com.megetood.redis.demo.repository;

import com.megetood.redis.demo.model.CoffeeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Lei Chengdong
 * @date 2020/12/21
 */
public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder, Long> {
}

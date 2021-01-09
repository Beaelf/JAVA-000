package com.megetood.redis.demo.service;

import com.megetood.redis.demo.enums.OrderState;
import com.megetood.redis.demo.model.Coffee;
import com.megetood.redis.demo.model.CoffeeOrder;
import com.megetood.redis.demo.repository.CoffeeOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Lei Chengdong
 * @date 2020/12/22
 */
@Slf4j
@Service
@Transactional
public class CoffeeOrderService {
    @Autowired
    private CoffeeOrderRepository orderRepository;

    public CoffeeOrder createOrder(String customer, Coffee... coffee) {
        CoffeeOrder order = CoffeeOrder.builder()
                .customer(customer)
                .items(new ArrayList<>(Arrays.asList(coffee)))
                .state(OrderState.INIT)
                .build();

        CoffeeOrder coffeeOrder = orderRepository.save(order);

        log.info("New Order: {}", coffee);
        return coffeeOrder;
    }

    public boolean updateState(CoffeeOrder order, OrderState state) {
        if (state.compareTo(order.getState()) <= 0) {
            log.warn("Wrong State order: {}, {}", state, order.getState());
            return false;
        }

        order.setState(state);
        orderRepository.save(order);
        log.info("Updated Order: {}", order);

        return true;
    }
}

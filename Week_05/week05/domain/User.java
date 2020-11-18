package com.megetood.geek.week05.domain;

import com.megetood.geek.week05.enums.City;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 用户类
 *
 * @author Lei Chengdong
 * @date 2020/10/21
 */
@Getter
@Setter
@ToString
public class User {
    private Long id;
    private String name;
    private City city;
    private City[] workCities;
    private List<City> lifeCities;
}

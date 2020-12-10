package com.megetood.geek.week07.dynamicdatasource.domain;

import com.megetood.geek.week07.dynamicdatasource.annotation.DataSource;

public interface ItemsMapper extends MyMapper<Items> {
    @DataSource
    Items selectByName(String name);

    @DataSource("slave1")  //slave1
    Items selectById(Long id);
}
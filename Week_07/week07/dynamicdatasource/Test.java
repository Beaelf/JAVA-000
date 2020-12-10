package com.megetood.geek.week07.dynamicdatasource;

import com.megetood.geek.week07.dynamicdatasource.domain.Items;
import com.megetood.geek.week07.dynamicdatasource.domain.ItemsMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Lei Chengdong
 * @date 2020/12/9
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class Test {
    @Autowired
    private ItemsMapper itemsMapper;

    @org.junit.Test
    public void select() {
        Items item = itemsMapper.selectById(1L);
        System.out.println(item);

        Items item2 = itemsMapper.selectByName("test001");
        System.out.println(item2);
    }
}

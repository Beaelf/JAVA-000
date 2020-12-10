package com.megetood.geek.week07.dynamicdatasource.service;

import com.megetood.geek.week07.dynamicdatasource.annotation.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 *
 * @author Lei Chengdong
 * @date 2020/12/9
 */
@Slf4j
@Aspect
@Component
public class DynamicDataSourceAspect {
    @Before("@annotation(ds)")
    public void changeDataSource(JoinPoint point, DataSource ds) throws Throwable {
        String dsId = ds.value();
        if (DynamicDataSourceContextHolder.dataSourceIds.contains(dsId)) {
             log.debug("Use DataSource :{} >", dsId, point.getSignature());
        } else {
             log.info("数据源[{}]不存在，使用默认数据源 >{}", dsId, point.getSignature());
            DynamicDataSourceContextHolder.setDataSourceRouterKey(dsId);
        }
    }

    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, DataSource ds) {
         log.debug("Revert DataSource : " + ds.value() + " > " + point.getSignature());
        DynamicDataSourceContextHolder.removeDataSourceRouterKey();

    }
}

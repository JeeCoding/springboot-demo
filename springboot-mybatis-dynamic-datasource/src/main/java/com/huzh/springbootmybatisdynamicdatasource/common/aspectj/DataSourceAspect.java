package com.huzh.springbootmybatisdynamicdatasource.common.aspectj;

import com.huzh.springbootmybatisdynamicdatasource.common.annotation.DataSource;
import com.huzh.springbootmybatisdynamicdatasource.common.config.datasource.DynamicDataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author huzh
 * @date 2020/10/12 10:54
 */
@Aspect
@Order(1)
@Component
public class DataSourceAspect {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(com.huzh.springbootmybatisdynamicdatasource.common.annotation.DataSource)"
            + "|| @within(com.huzh.springbootmybatisdynamicdatasource.common.annotation.DataSource)")
    public void dsPointCut() {

    }

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        DataSource dataSource = getDataSource(point);

        if (!StringUtils.isEmpty(dataSource)) {
            logger.debug("Use DataSource :{} >", dataSource.value().name());
            DynamicDataSourceContextHolder.setDataSourceType(dataSource.value().name());
        }

        try {
            return point.proceed();
        } finally {
            // 销毁数据源 在执行方法之后
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }

    /**
     * 获取需要切换的数据源
     */
    public DataSource getDataSource(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<? extends Object> targetClass = point.getTarget().getClass();
        DataSource targetDataSource = targetClass.getAnnotation(DataSource.class);
        if (!StringUtils.isEmpty(targetDataSource)) {
            return targetDataSource;
        } else {
            Method method = signature.getMethod();
            DataSource dataSource = method.getAnnotation(DataSource.class);
            return dataSource;
        }
    }
}
package com.v1.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * </p>
 *
 * @author lz
 * @date 2023/7/22 16:17
 */
@Component
public class SpringBeanUtils implements ApplicationContextAware {
    public static ApplicationContext applicationContext;
    @Override
    public  void setApplicationContext(ApplicationContext applicationContext){
        SpringBeanUtils.applicationContext = applicationContext;
    }
}

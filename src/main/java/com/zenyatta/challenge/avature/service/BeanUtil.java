package com.zenyatta.challenge.avature.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class BeanUtil implements ApplicationContextAware {

    private static final Object LOCK = new Object();
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext applicationContext) throws BeansException {
        synchronized (LOCK) {
            if (context == null) {
                context = applicationContext;
            }
        }
    }

    public static <T> T getBean(final Class<T> beanClass) {
        return context.getBean(beanClass);
    }
}

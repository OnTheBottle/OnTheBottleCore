package com.bottle.properties.conf;

import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by Sergey on 26.01.2017.
 */
@Slf4j
public class SpringVerticleFactory implements VerticleFactory, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public boolean blockingCreate() {
        // Usually verticle instantiation is fast but since our verticles are Spring Beans,
        // they might depend on other beans/resources which are slow to build/lookup.
        return true;
    }

    @Override
    public String prefix() {
        // Just an arbitrary string which must uniquely identify the verticle factory
        return "spring";
    }

    @Override
    public Verticle createVerticle(String verticleName, ClassLoader classLoader) throws Exception {
        String beanName = VerticleFactory.removePrefix(verticleName);
        log.debug(String.format("Getting Verticle bean (%s) from context", beanName));
        return (Verticle) applicationContext.getBean(beanName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

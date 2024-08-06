package com.interface21.context.support;

import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.context.ApplicationContext;
import org.reflections.Reflections;

import java.util.Set;

public class AnnotationConfigWebApplicationContext implements ApplicationContext {

    private final DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(final String... basePackages) {
        Reflections reflections = new Reflections(basePackages);
        this.beanFactory = new DefaultListableBeanFactory(reflections);
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return null;
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return Set.of();
    }
}

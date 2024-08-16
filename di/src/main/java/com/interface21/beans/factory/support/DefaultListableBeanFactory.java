package com.interface21.beans.factory.support;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.BeanDefinitions;
import com.interface21.beans.factory.config.SimpleBeanDefinition;
import com.interface21.beans.factory.config.SingletonRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultListableBeanFactory implements BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);
    private final BeanDefinitions beanDefinitions;
    private final SingletonRegistry singletonRegistry;
    private final String[] basePackages;

    public DefaultListableBeanFactory(final String... basePackages) {
        this.basePackages = basePackages;
        this.beanDefinitions = new BeanDefinitions();
        this.singletonRegistry = new SingletonRegistry();
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitions.getBeanClasses();
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return clazz.cast(singletonRegistry.getSingleton(clazz));
    }

    public void initialize() {
        log.info("Initializing beans");
        BeanScanner beanScanner = new BeanScanner(basePackages);
        Set<Class<?>> beanClasses = beanScanner.scan();
        beanDefinitions.init(beanClasses);
        getBeanClasses().forEach(this::initBean);
    }

    private Object initBean(final Class<?> beanClass) {
        final Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(beanClass, getBeanClasses())
                .orElseThrow(() -> new BeanInstantiationException(beanClass, "Could not autowire. No concrete class found for %s.".formatted(beanClass.getName())));
        Constructor<?> constructor = beanDefinitions.getConstructor(concreteClass);
        final Object createdBean = createBean(constructor);
        singletonRegistry.put(beanClass, createdBean);
        return createdBean;
    }

    private Object createBean(final Constructor<?> constructor) {
        try {
            return constructor.newInstance(createConstructorArgs(constructor));
        } catch (final Exception e) {
            throw new BeanInstantiationException(constructor.getDeclaringClass(), e.getMessage(), e);
        }
    }

    private Object[] createConstructorArgs(Constructor<?> constructor) {
        return Stream.of(constructor.getParameterTypes())
                .map(this::getOrCreateBean)
                .toArray();
    }

    private Object getOrCreateBean(Class<?> parameterType) {
        if (singletonRegistry.isContainsKey(parameterType)) {
            return singletonRegistry.getSingleton(parameterType);
        }
        return initBean(parameterType);
    }

    @Override
    public void clear() {
        log.info("Clearing beans");
        beanDefinitions.clear();
        singletonRegistry.clear();
    }
}

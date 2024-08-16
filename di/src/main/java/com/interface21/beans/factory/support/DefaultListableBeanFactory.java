package com.interface21.beans.factory.support;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.config.BeanDefinition;
import com.interface21.beans.factory.config.SimpleBeanDefinition;
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
    private final Map<Class<?>, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private final Map<Class<?>, Object> singletonObjects = new HashMap<>();
    private final String[] basePackages;

    public DefaultListableBeanFactory(final String... basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionMap.values().stream()
                .map(BeanDefinition::getType)
                .collect(Collectors.toSet());
    }

    @Override
    public <T> T getBean(final Class<T> clazz) {
        return clazz.cast(singletonObjects.get(clazz));
    }

    public void initialize() {
        log.info("Initializing beans");
        BeanScanner beanScanner = new BeanScanner(basePackages);
        Set<Class<?>> beanClasses = beanScanner.scan();
        beanClasses.forEach(beanClass -> {
            final BeanDefinition beanDefinition = new SimpleBeanDefinition(beanClass);
            beanDefinitionMap.put(beanClass, beanDefinition);
        });

        getBeanClasses().forEach(this::initBean);
    }

    private Object initBean(final Class<?> beanClass) {
        final Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(beanClass, getBeanClasses())
                .orElseThrow(() -> new BeanInstantiationException(beanClass, "Could not autowire. No concrete class found for %s.".formatted(beanClass.getName())));
        final Object createdBean = createBean(beanDefinitionMap.get(concreteClass).getConstructor());
        singletonObjects.put(beanClass, createdBean);
        return createdBean;
    }

    private Object createBean(final Constructor<?> constructor) {
        try {
            Object object = constructor.newInstance(createConstructorArgs(constructor));
            return object;
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
        if (singletonObjects.containsKey(parameterType)) {
            return singletonObjects.get(parameterType);
        }
        return initBean(parameterType);
    }

    @Override
    public void clear() {
        log.info("Clearing beans");
        beanDefinitionMap.clear();
        singletonObjects.clear();
    }
}

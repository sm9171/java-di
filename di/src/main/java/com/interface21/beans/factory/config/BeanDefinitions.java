package com.interface21.beans.factory.config;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanDefinitions {
    private final Map<Class<?>, BeanDefinition> beanDefinitionMap;

    public BeanDefinitions(Map<Class<?>, BeanDefinition> beanDefinitionMap) {
        this.beanDefinitionMap = beanDefinitionMap;
    }

    public BeanDefinitions() {
        this(new HashMap<>());
    }

    public void init(final Set<Class<?>> beanClasses) {
        beanClasses.forEach(this::registerBeanDefinition);
    }

    public void clear() {
        beanDefinitionMap.clear();
    }

    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionMap.values().stream()
                .map(BeanDefinition::getType)
                .collect(Collectors.toSet());
    }

    public Constructor<?> getConstructor(final Class<?> concreteClass) {
        return beanDefinitionMap.get(concreteClass).getConstructor();
    }

    private void registerBeanDefinition(Class<?> clazz) {
        final BeanDefinition beanDefinition = new SimpleBeanDefinition(clazz);
        beanDefinitionMap.put(clazz, beanDefinition);
    }
}

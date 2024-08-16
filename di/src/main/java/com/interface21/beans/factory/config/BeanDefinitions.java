package com.interface21.beans.factory.config;

import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.support.BeanFactoryUtils;

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

    private void registerBeanDefinition(Class<?> clazz) {
        final BeanDefinition beanDefinition = SimpleBeanDefinition.from(clazz);
        beanDefinitionMap.put(clazz, beanDefinition);
    }

    public BeanDefinition getBeanDefinition(Class<?> beanClass) {
        final BeanDefinition beanDefinition = beanDefinitionMap.get(beanClass);
        if (beanDefinition != null) {
            return beanDefinition;
        }

        final Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(beanClass, getBeanClasses())
                .orElseThrow(() -> new BeanInstantiationException(beanClass, "Could not autowire. No concrete class found for %s.".formatted(beanClass.getName())));

        final BeanDefinition concreteBeanDefinition = beanDefinitionMap.get(concreteClass);
        if (concreteBeanDefinition == null) {
            throw new BeanInstantiationException(beanClass, "cannot find bean for " + beanClass.getName());
        }

        return concreteBeanDefinition;
    }
}

package com.interface21.beans.factory.config;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class SingletonRegistry {
    private final Map<Class<?>, Object> singletonObjects;

    public SingletonRegistry(Map<Class<?>, Object> singletonObjects) {
        this.singletonObjects = singletonObjects;
    }

    public SingletonRegistry() {
        this.singletonObjects = new HashMap<>();
    }

    public Object getSingleton(Class<?> clazz) {
        return singletonObjects.get(clazz);
    }

    public void clear() {
        singletonObjects.clear();
    }

    public boolean isContainsKey(final Class<?> parameterType) {
        return singletonObjects.containsKey(parameterType);
    }

    public void put(final Class<?> beanClass, final Object createdBean) {
        singletonObjects.put(beanClass, createdBean);
    }
}

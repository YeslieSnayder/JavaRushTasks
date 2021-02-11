package com.javarush.task.task34.task3408;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

public class Cache<K, V> {
    private Map<K, V> cache = new WeakHashMap<>();

    public V getByKey(K key, Class<V> clazz) throws Exception {
        V obj = cache.get(key);
        if (obj == null) {
            Class[] params = {key.getClass()};
            cache.put(key, clazz.getConstructor(params).newInstance(key));
            obj = cache.get(key);
        }
        return obj;
    }

    public boolean put(V obj) {
        try {
            Method method = obj.getClass().getDeclaredMethod("getKey");
            method.setAccessible(true);
            K key = (K) method.invoke(obj);
            cache.put(key, obj);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return false;
        }
        return true;
    }

    public int size() {
        return cache.size();
    }
}

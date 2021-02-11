package com.javarush.task.task36.task3610;

import java.io.Serializable;
import java.util.*;

public class MyMultiMap<K, V> extends HashMap<K, V> implements Cloneable, Serializable {
    static final long serialVersionUID = 123456789L;
    private HashMap<K, List<V>> map;
    private int repeatCount;

    public MyMultiMap(int repeatCount) {
        this.repeatCount = repeatCount;
        map = new HashMap<>();
    }

    @Override
    public int size() {
        int size = 0;
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            size += entry.getValue().size();
        }
        return size;
    }

    @Override
    public V put(K key, V value) {
        List<V> list;
        if (!map.containsKey(key)) {
            list = new ArrayList<>();
            list.add(value);
            map.put(key, list);
            return null;
        } else if (map.containsKey(key) && (list = map.get(key)).size() < repeatCount) {
            list.add(value);
        } else if (map.containsKey(key) && (list = map.get(key)).size() == repeatCount) {
            list.remove(0);
            list.add(value);
        } else
            return null;

        return (list.size() > 1) ? list.get(list.size()-2) : list.get(0);
    }

    @Override
    public V remove(Object key) {
        if (!map.containsKey(key)) return null;

        V element = (map.get(key).size() == 0) ? null : map.get(key).remove(0);
        if (map.get(key).size() == 0)
            map.remove(key);

        return element;
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        ArrayList<V> list = new ArrayList<>();
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            list.addAll(entry.getValue());
        }
        return list;
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            for (V val : entry.getValue()) {
                if (value.equals(val)) return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            for (V v : entry.getValue()) {
                sb.append(v);
                sb.append(", ");
            }
        }
        String substring = sb.substring(0, sb.length() - 2);
        return substring + "}";
    }
}
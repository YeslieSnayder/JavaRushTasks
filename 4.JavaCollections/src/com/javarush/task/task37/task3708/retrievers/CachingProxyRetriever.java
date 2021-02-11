package com.javarush.task.task37.task3708.retrievers;

import com.javarush.task.task37.task3708.cache.LRUCache;
import com.javarush.task.task37.task3708.storage.Storage;

public class CachingProxyRetriever implements Retriever {
    private OriginalRetriever original;
    private LRUCache<Long, Object> cache;

    public CachingProxyRetriever(Storage storage) {
        original = new OriginalRetriever(storage);
        cache = new LRUCache<>(100);
    }

    @Override
    public Object retrieve(long id) {
        Object obj = cache.find(id);
        if (obj == null) {
            obj = original.retrieve(id);
            cache.set(id, obj);
        }
        return obj;
    }
}

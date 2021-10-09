package com.ywh.ds.concurrent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.*;

/**
 * 基于信号量实现缓存
 *
 * @author ywh
 * @since 4/8/2021
 */
public class Cache<K, V> {

    private final LRUMap<K, V> map;

    private final ReadWriteLock rwl = new ReentrantReadWriteLock();

    private final Lock r = rwl.readLock(), w = rwl.writeLock();

    /**
     * @param size
     */
    public Cache(int size) {
        this.map = new LRUMap<>(size);
    }

    /**
     * 读缓存
     *
     * @param key
     * @return
     */
    public V get(K key) {
        V v;
        // 读缓存
        r.lock();
        try {
            v = map.get(key);
        } finally {
            r.unlock();
        }
        // 缓存中存在，返回。
        if (v != null) {
            return v;
        }

        // 缓存中不存在，查询数据库。
        w.lock();
        try {
            // 再次验证：其他线程可能已经查询过数据库
            v = map.get(key);
            if (v == null) {
                // 省略查询数据库，更新缓存。
                map.put(key, v);
            }
        } finally {
            w.unlock();
        }
        return v;
    }

    /**
     * 写缓存
     *
     * @param key
     * @param value
     * @return
     */
    public V put(K key, V value) {
        w.lock();
        try {
            return map.put(key, value);
        } finally {
            w.unlock();
        }
    }

    /**
     * 锁的升级降级
     */
    volatile boolean cacheValid;

    /**
     *
     */
    public void processCachedData() {
        // 获取读锁
        r.lock();
        if (!cacheValid) {
            // 释放读锁，因为不允许读锁的升级
            r.unlock();
            // 获取写锁
            w.lock();
            try {
                // 再次检查状态
                if (!cacheValid) {
                    // data = ...
                    cacheValid = true;
                }
                // 释放写锁前，降级为读锁
                r.lock();
            } finally {
                // 释放写锁
                w.unlock();
            }
        }

        try {
            // 此处仍然持有读锁
            // use(data);
        } finally {
            r.unlock();
        }
    }

    /**
     * @param <K>
     * @param <V>
     */
    public static class LRUMap<K, V> extends LinkedHashMap<K, V> {

        private final int MAX_CACHE_SIZE;

        public LRUMap(int cacheSize) {
            super((int) Math.ceil(cacheSize / 0.75) + 1, 0.75f, true);
            MAX_CACHE_SIZE = cacheSize;
        }

        /**
         * @param eldest
         * @return
         */
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > MAX_CACHE_SIZE;
        }
    }


}
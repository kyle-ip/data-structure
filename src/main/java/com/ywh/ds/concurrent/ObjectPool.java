package com.ywh.ds.concurrent;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

/**
 * 基于信号量实现限流器
 *
 * @author ywh
 * @since 4/8/2021
 */
public class ObjectPool<T, R> {

    private final CopyOnWriteArrayList<T> pool;

    private final Semaphore semaphore;

    /**
     * 构造方法，初始放入 size 个 obj。
     *
     * @param size
     * @param obj
     */
    public ObjectPool(int size, T obj) {
        pool = new CopyOnWriteArrayList<>();
        for (int i = 0; i < size; i++) {
            pool.add(obj);
        }
        semaphore = new Semaphore(size);
    }

    /**
     * 利用对象池的对象，调用 func。
     *
     * @param func
     * @return
     * @throws InterruptedException
     */
    public R exec(Function<T, R> func) throws InterruptedException {
        T obj = null;
        semaphore.acquire();
        try {
            obj = pool.remove(0);
            return func.apply(obj);
        } finally {
            pool.add(obj);
            semaphore.release();
        }
    }

    /**
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        ObjectPool<Long, String> pool = new ObjectPool<>(10, 2L);
        String ret = pool.exec(t -> {
            System.out.println(t);
            return t.toString();
        });
        System.out.println(ret);
    }
}

package com.ywh.ds.queue;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 阻塞队列
 *
 * @author ywh
 * @since 4/8/2021
 */
public class BlockingQueue<T> {

    private final LinkedList<T> queue = new LinkedList<>();

    private final int size;

    /**
     *
     * @param size
     */
    public BlockingQueue(int size) {
        this.size = size;
    }

    private final Lock lock = new ReentrantLock();

    /**
     * 条件变量：队列非满、队列非空。
     */
    private final Condition notFull = lock.newCondition(), notEmpty = lock.newCondition();

    /**
     *
     * @param val
     * @throws InterruptedException
     */
    public void add(T val) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == size) {
                // 等待队列不满
                notFull.await();
            }
            queue.addLast(val);

            // 入队后可通知出队。
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     *
     * @return
     * @throws InterruptedException
     */
    public T poll() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                // 等待队列不空
                notEmpty.await();
            }
            T val = queue.removeLast();
            // 出队后可通知入队。
            notFull.signal();
            return val;
        } finally {
            lock.unlock();
        }
    }
}

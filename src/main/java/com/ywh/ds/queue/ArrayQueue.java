package com.ywh.ds.queue;

/**
 * 顺序队列
 *
 * @author ywh
 * @since 2020/10/29/029
 */
public class ArrayQueue<T> implements Queue<T> {

    private int head, tail;

    private final Object[] array;

    int count;

    int n;

    public ArrayQueue(int n) {
        this.array = new Object[n];
        this.head = 0;
        this.tail = 0;
        this.count = 0;
        this.n = n;
    }

    @Override
    public void enqueue(T val) {
        if (count == n) {
            throw new RuntimeException();
        }
        array[tail++] = val;
    }

    @Override
    public T dequeue() {
        if (head == tail) {
            throw new RuntimeException();
        }
        return (T) array[head++];
    }

    @Override
    public int size() {
        return n;
    }
}

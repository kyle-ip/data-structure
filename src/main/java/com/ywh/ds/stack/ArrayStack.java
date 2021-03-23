package com.ywh.ds.stack;

/**
 * 顺序栈
 *
 * @author ywh
 * @since 2020/10/29/029
 */
public class ArrayStack<T> implements Stack<T> {

    /**
     * 数组
     */
    private final Object[] array;

    /**
     * 元素个数（栈顶指针）
     */
    private int count;

    /**
     * 栈大小
     */
    private final int n;

    public ArrayStack(int n) {
        this.array = new Object[n];
        this.n = n;
        this.count = 0;
    }

    /**
     * 入栈
     *
     * @param val
     */
    @Override
    public void push(T val) {
        // 数组空间不足，直接返回。
        if (count == n) {
            throw new RuntimeException();
        }
        // 添加元素后栈顶指针 +1
        array[count++] = val;
    }

    /**
     * 出栈
     *
     * @return
     */
    @Override
    public T pop() {
        if (count == 0) {
            throw new RuntimeException();
        }
        return (T) array[--count];
    }

    /**
     *
     * @return
     */
    @Override
    public int size() {
        return count;
    }
}

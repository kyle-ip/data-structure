package com.ywh.ds.stack;

/**
 * 栈接口
 *
 * @author ywh
 * @since 2020/11/11/011
 */
public interface Stack<T> {

    /**
     * 入栈
     *
     * @param val
     */
    void push(T val);

    /**
     * 出栈
     *
     * @return
     */
    T pop();

    /**
     * 栈长度
     *
     * @return
     */
    int size();

}

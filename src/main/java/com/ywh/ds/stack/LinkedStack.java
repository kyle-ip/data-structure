package com.ywh.ds.stack;

import com.ywh.ds.list.ListNode;

/**
 * 链栈（头插法）
 *
 * @author ywh
 * @since 2020/10/29/029
 */
public class LinkedStack<T> implements Stack<T> {

    private ListNode<T> top;

    private int count;

    public LinkedStack() {
        top = null;
        count = 0;
    }

    /**
     * 入栈
     *
     * @param val
     * @return
     */
    @Override
    public void push(T val) {
        //            newNode
        //      栈顶    [ ] -> [ ]    栈底
        //                 <=  top
        ListNode<T> newNode = new ListNode<>(val);
        if (top == null) {
            top = newNode;
        } else {
            newNode.next = top;
            top = newNode;
        }
        count++;
    }

    /**
     * 出栈
     *
     * @return
     */
    @Override
    public T pop() {
        if (top == null) {
            throw new RuntimeException();
        }
        //              ret
        //      栈顶    [ ] -> [ ]    栈底
        //              top =>
        T ret = top.val;
        top = top.next;
        count--;
        return ret;
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

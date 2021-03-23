package com.ywh.ds.list;

import java.util.Objects;

/**
 * 单链表节点
 *
 * @author ywh
 * @since 2/14/2019
 */
public class ListNode<T> {

    public T val;

    public ListNode<T> next;

    public ListNode(){}

    public ListNode(T val) {
        this.val = val;
    }

    public ListNode(T val, ListNode<T> next) {
        this.val = val;
        this.next = next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ListNode)) {
            return false;
        }
        ListNode<?> listNode = (ListNode<?>) o;
        return val.equals(listNode.val) && next.equals(listNode.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(val, next);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        ListNode<T> cur = this;
        while (cur != null) {
            sb.append(cur.val);
            if (cur.next != null) {
                sb.append(",");
            }
            cur = cur.next;
        }
        return sb.toString();
    }
}

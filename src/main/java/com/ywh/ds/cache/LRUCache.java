package com.ywh.ds.cache;

import com.ywh.ds.list.DoublyListNode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ywh
 * @since 19/03/2021
 */
public class LRUCache {

    // 使用双向链表（移动节点 O(1)）和哈希表（get、put 操作 O(1)）存储。
    // 头节点存放下次 put 操作时被淘汰的元素，其 next 表示最近刚被使用的，prev 表示 LRU。

    /**
     *  prev: <-
     *  next: ->
     *
     *         +----------------------------------+
     *         ↓                                  ↓
     *      [node0] <-> [node1] <-> [node2] <-> [node3]
     * （最近刚被使用的）                   （head，存放 LRU）
     */
    private DoublyListNode head;

    private final Map<Integer, DoublyListNode> map;

    /**
     * 辅助方法，把节点移动到头节点的后面，表示最近刚被使用。
     *
     * @param node
     */
    private void moveToHeadNext(DoublyListNode node) {
        // 如果当前节点为头节点，表示头节点刚被使用，把头节点移动到其前一个节点。
        if (node == head) {
            head = head.prev;
            return;
        }
        // 否则解除当前节点的前驱、后继指针，把当前节点插到头节点的后面（六步），即 [head] <-> [cur] <-> [xxx]。

        // detach
        node.prev.next = node.next;
        node.next.prev = node.prev;

        // attach
        node.next = head.next;
        node.next.prev = node;

        head.next = node;
        node.prev = head;
    }

    /**
     * 构造方法，初始化链表。
     *
     * @param capacity
     */
    public LRUCache(int capacity) {
        head = new DoublyListNode(-1, -1, null, null);
        map = new HashMap<>();
        DoublyListNode node = head;
        // 尾插法创建容量 -1 个节点（因为 head 本身也用于存放数据）。
        for (int i = 0; i < capacity - 1; i++) {
            node.next = new DoublyListNode(-1, -1, null, node);
            node = node.next;
        }
        node.next = head;
        head.prev = node;
    }

    /**
     * 获取元素：
     * 从哈希表中取出节点，并把该节点移动到链表头部，返回该节点的值。
     *
     * @param key
     * @return
     */
    public int get(int key) {
        if (!map.containsKey(key)) {
            return -1;
        }
        DoublyListNode node = map.get(key);
        moveToHeadNext(node);
        return node.val;
    }

    /**
     * 添加元素
     *
     * @param key
     * @param value
     */
    public void put(int key, int value) {
        DoublyListNode node;
        // 哈希表中已存在该 key，则取出该节点。
        if (map.containsKey(key)) {
            node = map.get(key);
        }
        // 哈希表中不存在该 key，则取头节点来存放新元素（哈希表中要移除头节点的旧值）。
        else {
            node = head;
            map.remove(head.key);
        }
        // 设值，添加到哈希表，并移动到头部。
        node.key = key;
        node.val = value;
        map.put(key, node);
        moveToHeadNext(node);
    }
}

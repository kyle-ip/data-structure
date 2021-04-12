package com.ywh.ds.cache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * LFU 缓存，双哈希表实现
 *
 * Time: O(1), Space: O(n)
 *
 * @since 4/12/2021
 */
class LFUCache {

    /**
     * 最少使用的频率、容量。
     */
    int minfreq, capacity;

    /**
     * 键 -> 缓存节点（使 get 操作 O(1)）
     */
    Map<Integer, FreqNode> keyTable;

    /**
     * 频率 -> 缓存节点链表（使 put 操作 O(1)）
     * freqTable/keyTable                1
     *          1           Node(key=1, value=1, freq=1)
     */
    Map<Integer, LinkedList<FreqNode>> freqTable;

    /**
     *
     * @param capacity
     */
    public LFUCache(int capacity) {
        this.minfreq = 0;
        this.capacity = capacity;
        keyTable = new HashMap<>(capacity);
        freqTable = new HashMap<>();
    }

    /**
     * 从 key 表取值，更新 freq 表。
     *
     * @param key
     * @return
     */
    public int get(int key) {
        // 判空
        if (capacity == 0 || !keyTable.containsKey(key)) {
            return -1;
        }
        // 从 key 表中取出节点（得出值和使用频率）并更新。
        FreqNode node = keyTable.get(key);
        updateNode(node);
        return node.val;
    }

    /**
     *
     * @param key
     * @param value
     */
    public void put(int key, int value) {
        if (capacity == 0) {
            return;
        }
        FreqNode node;

        // key 表中不存在该节点。
        if (!keyTable.containsKey(key)) {
            // 表已满，清理访问频率最低的节点。
            if (keyTable.size() == capacity) {
                node = freqTable.get(minfreq).getLast();
                keyTable.remove(node.key);
                freqTable.get(minfreq).removeLast();
                if (freqTable.get(minfreq).size() == 0) {
                    freqTable.remove(minfreq);
                }
            }
            node = new FreqNode(key, value, 1);
            minfreq = 1;
            addNode(node);
        }
        // key 表中已存在该节点，访问频率已变，需要更新节点。
        else {
            node = keyTable.get(key);
            node.val = value;
            updateNode(node);
        }

    }

    /**
     * 更新节点（访问频率 +1）。
     *
     * @param node
     */
    private void updateNode(FreqNode node) {
        // 1. 从 freq 表中删除节点。如果删除后该链表为空，则还需要在哈希表中删除并更新 minFreq。
        // 2. 访问后频率 +1，重新插入到 freq 表和 key 表中。
        int freq = node.freq;
        freqTable.get(freq).remove(node);
        if (freqTable.get(freq).size() == 0) {
            freqTable.remove(freq);
            if (minfreq == freq) {
                minfreq += 1;
            }
        }
        node.freq++;
        addNode(node);
    }



    /**
     * 插入节点到 freq 表和 key 表。
     *
     * @param node
     */
    private void addNode(FreqNode node) {
        LinkedList<FreqNode> list = freqTable.getOrDefault(node.freq, new LinkedList<>());
        list.addFirst(node);
        freqTable.put(node.freq, list);
        keyTable.put(node.key, node);
    }
}

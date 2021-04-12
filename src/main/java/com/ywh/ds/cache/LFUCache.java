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
        keyTable = new HashMap<>();
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
        // 从 key 表中取出节点（得出值和使用频率）。
        FreqNode node = keyTable.get(key);
        int val = node.val, freq = node.freq;

        // 更新 freq 表：
        // 1. 从 freq 表中删除节点。
        freqTable.get(freq).remove(node);
        // 2. 如果当前链表为空，需要在哈希表中删除并更新 minFreq。
        if (freqTable.get(freq).size() == 0) {
            freqTable.remove(freq);
            if (minfreq == freq) {
                minfreq += 1;
            }
        }
        // 3. 访问后频率 +1，重新插入到 freq 表和 key 表中。
        node.freq += 1;

        LinkedList<FreqNode> list = freqTable.getOrDefault(node.freq, new LinkedList<>());
        list.addFirst(node);
        freqTable.put(node.freq, list);
        keyTable.put(key, node);
        return val;
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
        int freq;
        FreqNode node;
        // key 表中不存在该节点。
        if (!keyTable.containsKey(key)) {
            // 缓存已满，需要进行删除操作
            if (keyTable.size() == capacity) {
                // 通过 minFreq 拿到 freq_table[minFreq] 链表的末尾节点
                node = freqTable.get(minfreq).getLast();
                keyTable.remove(node.key);
                freqTable.get(minfreq).pollLast();
                if (freqTable.get(minfreq).size() == 0) {
                    freqTable.remove(minfreq);
                }
            }
            freq = 1;
            minfreq = 1;
        }
        // key 表中已存在该节点。
        else {
            node = keyTable.get(key);
            freq = node.freq;
            freqTable.get(freq).remove(node);
            if (freqTable.get(freq).size() == 0) {
                freqTable.remove(freq);
                if (minfreq == freq) {
                    minfreq += 1;
                }
            }
            freq += 1;
        }

        // 重新加入 key 表、freq 表。
        node = new FreqNode(key, value, freq);
        LinkedList<FreqNode> list = freqTable.getOrDefault(node.freq, new LinkedList<>());
        list.addFirst(node);
        freqTable.put(node.freq, list);
        keyTable.put(key, node);
    }
}

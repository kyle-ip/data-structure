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
     * 最低访问频率、容量。
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
        if (capacity == 0 || !keyTable.containsKey(key)) {
            return -1;
        }
        // 从 key 表中取出节点并更新使用频率。
        FreqNode node = keyTable.get(key);
        updateNode(node, node.val);
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

        // key 表中已存在该节点，则更新。
        if (keyTable.containsKey(key)) {
            updateNode(keyTable.get(key), value);

        }
        // key 表中不存在该节点，则插入新节点（更新最低访问频率）。
        else {
            // 表已满，清理访问频率最低的节点。
            if (keyTable.size() == capacity) {
                FreqNode node = freqTable.get(minfreq).getLast();
                keyTable.remove(node.key);
                freqTable.get(minfreq).removeLast();
                if (freqTable.get(minfreq).size() == 0) {
                    freqTable.remove(minfreq);
                }
            }
            minfreq = 1;
            addNode(new FreqNode(key, value, 1));
        }

    }

    /**
     * 更新节点（设置新值、更新频率）
     *
     * @param node
     * @param value
     */
    private void updateNode(FreqNode node, int value) {
        node.val = value;

        // 1. 从 freq 表中删除节点。如果删除后该链表为空，则还需要在哈希表中删除并更新 minFreq。
        freqTable.get(node.freq).remove(node);
        if (freqTable.get(node.freq).size() == 0) {
            freqTable.remove(node.freq);
            if (minfreq == node.freq) {
                minfreq += 1;
            }
        }

        // 2. 访问后频率 +1，重新插入到 freq 表和 key 表中。
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


    /**
     * 带访问频率的缓存节点
     */
    private class FreqNode {

        int key, val, freq;

        FreqNode(int key, int val, int freq) {
            this.key = key;
            this.val = val;
            this.freq = freq;
        }
    }
}

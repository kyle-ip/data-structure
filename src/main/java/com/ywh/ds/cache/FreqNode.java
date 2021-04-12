package com.ywh.ds.cache;

/**
 * 带访问频率的缓存节点
 *
 * @author ywh
 * @since 4/12/2021
 */
public class FreqNode {

    int key, val, freq;

    FreqNode(int key, int val, int freq) {
        this.key = key;
        this.val = val;
        this.freq = freq;
    }
}

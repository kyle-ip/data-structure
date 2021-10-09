package com.ywh.ds.hash;

/**
 * @author ywh
 * @since 4/12/2021
 */
public class HashFunction {

    /**
     * 默认为上面的 DEFAULT_SIZE
     */
    private final int size;

    /**
     * 即上面的 seeds
     */
    private final int seed;

    public HashFunction(int size, int seed) {
        this.size = size;
        this.seed = seed;
    }

    /**
     * 计算 hash 值
     *
     * @param value
     * @return
     */
    public int hash(String value) {
        int ret = 0;
        // 比如 seed 为 11，输入 ABC，即：11*0+65 + 11*(11*0+65)+66 + 11*(11*(11*0+65)+66)+67
        for (int i = 0; i < value.length(); i++) {
            ret = seed * ret + value.charAt(i);
        }
        return (size - 1) & ret;
    }

}
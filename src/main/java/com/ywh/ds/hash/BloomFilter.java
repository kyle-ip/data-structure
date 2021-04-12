package com.ywh.ds.hash;

import java.util.BitSet;

/**
 * @author ywh
 * @since 19/03/2021
 */
public class BloomFilter {

    /**
     * 长度为 1_073_741_824 的比特位
     */
    final int DEFAULT_SIZE = 2 << 29;

    /**
     * 为了降低错误率，使用加法 hash 算法，所以定义一个 8 个元素的质数数组
     */
    public static final int[] seeds = {3, 5, 7, 11, 13, 31, 37, 61};

    /**
     * 8 个不同的 hash 函数。
     */
    private final HashFunction[] functions = new HashFunction[seeds.length];

    /**
     * Bitmap
     */
    private final BitSet bitmap = new BitSet(DEFAULT_SIZE);

    /**
     *
     */
    public BloomFilter() {
        for (int i = 0; i < seeds.length; i++) {
            functions[i] = new HashFunction(DEFAULT_SIZE, seeds[i]);
        }
    }

    /**
     * 添加数据
     *
     * @param value 需要加入的值
     */
    public void add(String value) {
        if (value != null) {
            for (HashFunction f : functions) {
                // 计算 hash 值并修改 bitmap 中相应位置为 true。
                bitmap.set(f.hash(value), true);
            }
        }
    }

    /**
     * 判断相应元素是否存在
     *
     * @param value 需要判断的元素
     * @return 结果
     */
    public boolean contains(String value) {
        if (value == null) {
            return false;
        }
        boolean ret = true;
        for (HashFunction f : functions) {
            ret = bitmap.get(f.hash(value));
            if (!ret) {
                break;
            }
        }
        return ret;
    }



    public static void main(String[] args) {
//        BloomFilter bf = new BloomFilter();
//        for (int i = 0; i < 100_000_000; i++) {
//            bf.add(String.valueOf(i));
//        }
//        String id = "123456789";
//        bf.add(id);
//
//        System.out.println(bf.contains(id));
//        System.out.println(bf.contains("234567890"));
        System.out.println(0 + 'C');
    }
}
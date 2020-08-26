package com.wym.guava;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;


public class BloomFilterTest {


    public static void main(String[] args) {
        int total = 100 * 10000;
        BloomFilter<CharSequence> filter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), total);
        for (int i = 0; i < total; i++) {
            filter.put(i + "");
        }
        int hitCount = 0;
        for (int i = 0; i < total + 10000; i++) {
            if (filter.mightContain(i + "")) {
                hitCount++;
            }
        }
        System.out.println("hitCount:" + hitCount);
        System.out.println("错误率：" + (hitCount - total) * 1.0 / total);
    }
}

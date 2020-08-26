package com.wym;

import lombok.val;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * LongAddr + CHM实现原子计数器
 */
public class AtomicCounter {


    public static void main(String[] args) {

        ConcurrentHashMap<String, LongAdder> freq = new ConcurrentHashMap<>();
        String key = "123";
        //下面的每行都是原子操作
        freq.computeIfAbsent(key, k -> new LongAdder()).increment();
        freq.computeIfAbsent(key, k -> new LongAdder()).increment();
        freq.computeIfAbsent(key, k -> new LongAdder()).increment();
        freq.computeIfAbsent(key, k -> new LongAdder()).increment();
        freq.computeIfAbsent(key, k -> new LongAdder()).increment();
    }
}

package com.wym.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class LoadingCacheTest {


    static LoadingCache<String, String> loadingCache =
            CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.DAYS).refreshAfterWrite(Duration.ofSeconds(7)).build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    System.out.println("load() abc");
                    return "123";
                }

                @Override
                public ListenableFuture<String> reload(String key, String oldValue) throws Exception {
                    System.out.println("reload()...");
                    ListenableFutureTask<String> task =
                            ListenableFutureTask.create(() -> load(key));
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.submit(task);
                    executorService.shutdown();
                    return task;
                }
            });

    public static void main(String[] args) throws Exception {

        int i = 0;
        while (++i < 10) {
            System.out.println(LocalDateTime.now() + " " + loadingCache.get("abc"));
            Thread.sleep(1000);
        }

        System.out.println("手动更改值");
        loadingCache.put("abc", "456");

        i = 0;
        while (++i < 10) {
            System.out.println(LocalDateTime.now() + " " + loadingCache.get("abc"));
            Thread.sleep(1000);
        }
    }
}

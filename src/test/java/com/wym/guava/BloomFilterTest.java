package com.wym.guava;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/**
 * 如果布隆过滤器判断一个数据不在过滤器中，则一定不在过滤器中，如果判断在过滤其中，则有很小可能不在过滤器中（存在一定误判率，默认0.03）
 *
 * 应用：
 * 1. 爬虫对URL去重，避免爬取相同URL的地址
 * 2. 将文章id加入布隆过滤器，避免向用户推荐用户已经读过的文章
 * 3. 反垃圾邮件，从数十亿垃圾邮件列表中判断某邮箱是否是垃圾邮箱
 * 4. 将数据主键缓存到布隆过滤器中，当根据ID进行查询时，先通过布隆过滤器判断数据是否存在，如果数据不存在，则直接返回，避免访问数据库，
 * 可以解决缓存穿透问题
 * 5. google chrome使用布隆过滤器识别恶意URL
 */
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

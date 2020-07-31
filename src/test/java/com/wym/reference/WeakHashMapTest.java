package com.wym.reference;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author v_ymmwu
 */
public class WeakHashMapTest {

    public static void main(String[] args) {
        method(new HashMap<>());

        System.out.println("-------------------------");

        method(new WeakHashMap<>());
    }

    static void method(Map<Integer, String> map) {
        Integer key = new Integer(1);  //注意，一定不要用Integer.valueOf(),因为会从缓存中取！！！！！！！！！-128 ~ 127被缓存
        String val = "123";

        map.put(key, val);
        System.out.println(map);

        key = null;
        System.out.println(map);

        System.gc();

        System.out.println(map + "\t" + map.size());

        System.out.println();


    }

}

package com.wym;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListTest {

    public static void main(String[] args) {
        List<String> list = new CopyOnWriteArrayList<>();

        list.add("111");
        list.add("222");
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        for (String s : list) {
            System.out.println(s);
        }
    }
}

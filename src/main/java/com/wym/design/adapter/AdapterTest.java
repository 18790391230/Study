package com.wym.design.adapter;


import java.util.Arrays;
import java.util.List;

public class AdapterTest {

    public static void main(String[] args) {
//        String abc = "支付宝";
        String abc = "weixin";
        List<RequestAdapter> requestAdapters = Arrays.asList(new ConcreteAdapterA(), new ConcreteAdapterB());
        for (RequestAdapter requestAdapter : requestAdapters) {
            if (requestAdapter.supports(abc)) {
                System.out.println(requestAdapter.handle("", abc));
            }
        }
    }
}

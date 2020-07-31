package com.wym.error;

import java.util.ArrayList;
import java.util.List;

/**
 *Exception in thread "main" java.lang.OutOfMemoryError: Requested array size exceeds VM limit
 * 	at com.wym.error.OutOfMemoryError.main(OutOfMemoryError.java:15)
 */
public class OutOfMemoryError {

    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();

        while (true) {
            list.add(new byte[Integer.MAX_VALUE]);
        }
    }
}

package com.wym.asynctest;

import com.wym.asynctest.service.IAsyncServiceA;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 *
 */
@SpringBootTest
public class AsyncTest {


    @Autowired
    private IAsyncServiceA asyncServiceA;

    @Test
    public void test1() throws IOException {
//        asyncServiceA.foo1();
        asyncServiceA.foo2();
//        asyncServiceA.foo3();
//        asyncServiceA.foo4();
        int read = System.in.read();
    }
}

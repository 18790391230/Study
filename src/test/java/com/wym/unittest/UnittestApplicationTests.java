package com.wym.unittest;

import com.wym.unittest.model.Account;
import com.wym.unittest.model.User;
import com.wym.unittest.service.IAccountService;
import com.wym.unittest.service.IThirdService;
import com.wym.unittest.service.IUserService;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.Assert;

import java.util.Collections;

import static org.mockito.Mockito.inOrder;


@SpringBootTest
class UnittestApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private IUserService userService;

    @MockBean
    private IAccountService accountService;

    @MockBean
    private IThirdService thirdService;

    @Test
    public void getUserById() {

        Account account = new Account();
        account.setId(111);
//        Mockito.when(accountService.getList(true)).thenReturn(Collections.singletonList(account));
        Mockito.when(accountService.getList(Mockito.anyBoolean())).thenReturn(Collections.singletonList(account));
        User user = userService.getById(10);

        Assert.notNull(user, "user为空！");
        System.out.println(user);
    }

    @Test
    public void verifyInvokeTimes() {

        //校验传某个参数的调用次数
        Account account = new Account();
        account.setId(111);
        //1.mock
        Mockito.when(accountService.getList(Mockito.anyBoolean())).thenReturn(Collections.singletonList(account));

        //2.invoke
        User user = userService.getById(10);

        //3.verify
        Mockito.verify(accountService, Mockito.times(1)).getList(Mockito.eq(true));
    }

    @Test
    public void verifyInvokeOrder() {

        Account account = new Account();
        account.setId(111);
        //1.mock
        Mockito.when(accountService.getList(Mockito.anyBoolean())).thenReturn(Collections.singletonList(account));

        //2.invoke
        userService.getById(10);

        //3.verify
        InOrder inOrder = inOrder(accountService, thirdService);
        inOrder.verify(accountService).getList(true);
        inOrder.verify(thirdService).getName();
    }
}

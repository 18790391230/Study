package com.wym.unittest.service.impl;

import com.wym.common.model.Account;
import com.wym.unittest.service.IAccountService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AccountServiceService implements IAccountService {
    @Override
    public List<Account> getList(boolean b) {
        Account a1 = new Account();
        a1.setId(100);
        Account a2 = new Account();
        a2.setId(2);
        return Arrays.asList(a1, a2);
    }
}

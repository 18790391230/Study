package com.wym.unittest.service.impl;


import com.wym.common.model.Account;
import com.wym.common.model.User;
import com.wym.unittest.service.IAccountService;
import com.wym.unittest.service.IThirdService;
import com.wym.unittest.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IThirdService thirdService;

    @Override
    public User getById(Integer id) {
        List<Account> list = accountService.getList(true);
        thirdService.getName();
        User user = new User();
        user.setName("我是user的名字");
        user.setId(list.get(0).getId());
        return user;
    }
}

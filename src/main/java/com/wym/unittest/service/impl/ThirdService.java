package com.wym.unittest.service.impl;

import com.wym.unittest.service.IThirdService;
import org.springframework.stereotype.Service;

@Service
public class ThirdService implements IThirdService {

    @Override
    public String getName() {
        return "I'm ThirdService";
    }
}

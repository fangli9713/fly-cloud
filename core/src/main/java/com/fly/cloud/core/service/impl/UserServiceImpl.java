package com.fly.cloud.core.service.impl;

import com.fly.cloud.core.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {


    @Override
    public String add() {
        return "core-add";
    }
}

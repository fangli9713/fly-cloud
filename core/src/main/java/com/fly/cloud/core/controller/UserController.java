package com.fly.cloud.core.controller;

import com.fly.cloud.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping("/user/add/{str}")
    public String add(@PathVariable String str){

        return str + "/" +userService.add();
    }
}

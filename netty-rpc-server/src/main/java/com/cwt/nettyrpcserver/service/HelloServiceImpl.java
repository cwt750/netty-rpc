package com.cwt.nettyrpcserver.service;


import com.cwt.service.HelloService;
import com.cwt.common.annotation.PRCServer;


@PRCServer(cls = HelloService.class)
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String msg) {
        return "hello echo: " + msg;
    }
}

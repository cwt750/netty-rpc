package com.cwt.nettyrpcserver.service;


import com.cwt.common.annotation.PRCServer;
import com.cwt.service.HiService;


@PRCServer(cls = HiService.class)
public class HiServiceImpl implements HiService {

    public String hi(String msg) {
        return "hi echo: " + msg;
    }
}

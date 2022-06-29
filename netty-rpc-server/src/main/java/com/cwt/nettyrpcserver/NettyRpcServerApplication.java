package com.cwt.nettyrpcserver;


import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class NettyRpcServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(NettyRpcServerApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

}

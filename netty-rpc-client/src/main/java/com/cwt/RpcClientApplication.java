package com.cwt;


import com.cwt.service.HelloService;
import com.cwt.service.HiService;

public class RpcClientApplication {

    public static void main(String[] args) {
        RpcInvocationHandler handler1=new RpcInvocationHandler(HiService.class);
        HiService proxy1=(HiService)handler1.getProxy();
        String msg1=proxy1.hi("cwt");
        System.out.println(msg1);

        RpcInvocationHandler handler2=new RpcInvocationHandler(HelloService.class);
        HelloService proxy2=(HelloService) handler2.getProxy();
        String msg2=proxy2.hello("cwt");
        System.out.println(msg2);
    }
}

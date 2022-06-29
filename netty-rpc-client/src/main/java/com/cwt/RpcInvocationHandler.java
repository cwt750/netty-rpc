package com.cwt;

import com.cwt.common.http.Request;
import com.cwt.common.http.Response;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

public class RpcInvocationHandler implements InvocationHandler {
    private Class<?> clazz;
    public RpcInvocationHandler(Class<?> clazz) {
        this.clazz=clazz;
    }
    public Object getProxy(){
        return Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[] {clazz},this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = new Request();
        request.setInterfaceName("/" + clazz.getName());
        request.setRequestId(UUID.randomUUID().toString());
        request.setParameter(args);
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());

        Response response = new NettyClient().client(request);
        return response.getResult();
    }


}

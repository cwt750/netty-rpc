package com.cwt.nettyrpcserver.server;

import com.alibaba.fastjson.JSON;
import com.cwt.common.http.Request;
import com.cwt.common.http.Response;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;


public class RpcServerInboundHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RpcServerInboundHandler.class);

    private final Map<String, Object> handle;

    public RpcServerInboundHandler(Map<String, Object> handle) {
        this.handle = handle;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = (Request) msg;
        logger.info("request data {}", JSON.toJSONString(request));


        Object bean = handle.get(request.getInterfaceName());
        Method method = bean.getClass().getMethod(request.getMethodName(), request.getParameterTypes());
        method.setAccessible(true);
        Object result = method.invoke(bean, request.getParameter());

        Response response = new Response();
        response.setRequestId(request.getRequestId());
        response.setResult(result);


        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}

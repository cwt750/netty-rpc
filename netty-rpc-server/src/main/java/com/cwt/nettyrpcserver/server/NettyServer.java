package com.cwt.nettyrpcserver.server;


import com.cwt.common.http.Request;
import com.cwt.common.http.Response;
import com.cwt.common.serialize.RpcDecoder;
import com.cwt.common.serialize.RpcEncoder;
import com.cwt.common.zookeeper.ZooKeeperOp;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class NettyServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    public static void start(String ip, int port, Map<String, Object> params) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline()
                                    .addLast(new RpcDecoder(Request.class))
                                    .addLast(new RpcEncoder(Response.class))
                                    .addLast(new RpcServerInboundHandler(params));
                        }
                    });

            ChannelFuture future = serverBootstrap.bind(ip, port).sync();
            if (future.isSuccess()) {
                params.keySet().forEach(key -> ZooKeeperOp.register(key, ip + ":" + port));
            }
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}

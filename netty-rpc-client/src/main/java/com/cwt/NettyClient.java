package com.cwt;


import com.cwt.common.http.Request;
import com.cwt.common.http.Response;
import com.cwt.common.serialize.RpcDecoder;
import com.cwt.common.serialize.RpcEncoder;
import com.cwt.common.zookeeper.ZooKeeperOp;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


public class NettyClient extends SimpleChannelInboundHandler<Response> {

    private Response response;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Response response) {
        this.response = response;
    }

    public Response client(Request request) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            // 创建并初始化 Netty 客户端 Bootstrap 对象
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) {
                            channel.pipeline()
                                    .addLast(new RpcDecoder(Response.class))
                                    .addLast(new RpcEncoder(Request.class))
                                    .addLast(NettyClient.this);
                        }
                    });

            // 连接 RPC 服务器
            String[] discover = ZooKeeperOp.discover(request.getInterfaceName()).split(":");
            ChannelFuture future = bootstrap.connect(discover[0], Integer.parseInt(discover[1])).sync();

            // 写入 RPC 请求数据并关闭连接
            Channel channel = future.channel();
            channel.writeAndFlush(request).sync();
            channel.closeFuture().sync();

            return response;
        } finally {
            group.shutdownGracefully();
        }
    }

}
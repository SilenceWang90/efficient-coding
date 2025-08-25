package com.wp.netty.springdemo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author wangpeng
 * @description Client
 * @date 2025/8/24 17:41
 **/
public class Client {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 关键：添加编解码器（必须与服务端一致）
                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));

                            // 添加业务处理器
                            pipeline.addLast(new ClientHandler());
                        }
                    });

            ChannelFuture f = b.connect("127.0.0.1", 8080).sync();
            System.out.println("客户端已连接到服务器...");
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}

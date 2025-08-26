package com.wp.netty.simpledemo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author wangpeng
 * @description Server
 * @date 2025/8/24 17:41
 **/
public class Server {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 关键：添加编解码器
                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8)); // 收：ByteBuf -> String
                            pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8)); // 发：String -> ByteBuf

                            // 添加业务处理器，现在可以直接处理String了
                            pipeline.addLast(new ServerHandler());
                        }
                    });

            ChannelFuture f = b.bind(8080).sync();
            System.out.println("Server服务器启动在端口 8080...");
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

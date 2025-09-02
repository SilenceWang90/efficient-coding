package com.wp.netty.protobufdemo.server;

import com.wp.protobuf.NettyCommunication;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

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
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            /** 解码器 **/
                            // ProtobufVarint32FrameDecoder从连续的TCP字节流中，根据消息头中的长度值，准确地切割出一个个独立、完整的Protobuf数据包，并将
                            // 这个纯净的数据包传递给下一个处理器（ProtobufDecoder）。
                            // ProtobufVarint32FrameDecoder解决了数据“从哪里开始，到哪里结束”的问题
                            pipeline.addLast(new ProtobufVarint32FrameDecoder());
                            // 设置解码结构，即对接收到的二进制数据流解析成何种类类型。此demo是服务器端对客户端的Request请求信息做解析
                            pipeline.addLast(new ProtobufDecoder(NettyCommunication.Request.getDefaultInstance()));
                            /** 编码器 **/
                            // ProtobufVarint32LengthFieldPrepender在已经序列化好的Protobuf二进制数据（ProtobufEncoder的输出）前面，预先
                            // 添加一个包含该数据长度信息的消息头，从而制造出一个可以被对方ProtobufVarint32FrameDecoder 识别和处理的、带有明确边界的数据帧。
                            // 它解决了“如何让对方知道我这个包有多大”的问题。
                            pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                            // 编码器
                            pipeline.addLast(new ProtobufEncoder());

                            /** 添加自定义的业务处理器 **/
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

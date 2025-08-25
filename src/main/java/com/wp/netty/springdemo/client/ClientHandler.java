package com.wp.netty.springdemo.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author wangpeng
 * @description ClientHandler
 * @date 2025/8/24 17:41
 **/
public class ClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 连接建立后，立即发送一条字符串消息
        String firstMessage = "Hello Server! 我是Netty客户端。";
        System.out.println("客户端发送: " + firstMessage);
        ctx.writeAndFlush(firstMessage); // 直接写入String！
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) { // msg直接就是String!
        // 直接打印从服务器收到的字符串回复
        System.out.println("客户端收到回复: " + msg);

        // 收到回复后，优雅地关闭连接
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

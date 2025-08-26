package com.wp.netty.simpledemo.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author wangpeng
 * @description ServerHandler
 * @date 2025/8/24 17:41
 **/
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("有客户端连接: " + ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) { // msg直接就是String!
        // 1. 直接打印收到的字符串
        System.out.println("服务器收到: " + msg);

        // 2. 直接回复一个字符串
        String response = "你好客户端，我收到了你的消息: '" + msg + "'";
        ctx.writeAndFlush(response); // 直接写入String！
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

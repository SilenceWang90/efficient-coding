package com.wp.netty.protobufdemo.server;

import com.wp.protobuf.NettyCommunication;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author wangpeng
 * @description ServerHandler
 * @date 2025/8/24 17:41
 **/
public class ServerHandler extends SimpleChannelInboundHandler<NettyCommunication.Request> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("有客户端连接: " + ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NettyCommunication.Request msg) {
        // 1. 直接打印收到的信息
        System.out.println("服务器收到: " + msg);

        // 2. 回复
        NettyCommunication.Response response = NettyCommunication.Response
                .newBuilder()
                .setId("2").setCode(2).setDesc("我是回复的消息")
                .build();
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

package com.wp.netty.protobufdemo.client;

import com.wp.protobuf.NettyCommunication;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author wangpeng
 * @description ClientHandler
 * @date 2025/8/24 17:41
 **/
public class ClientHandler extends SimpleChannelInboundHandler<NettyCommunication.Response> {
    /**
     * 建立连接后的处理
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 创建消息对象
        NettyCommunication.Request request = NettyCommunication.Request
                .newBuilder()
                .setId("1").setData("data").setSequence(1)
                .build();

        ctx.writeAndFlush(request);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NettyCommunication.Response msg) {
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

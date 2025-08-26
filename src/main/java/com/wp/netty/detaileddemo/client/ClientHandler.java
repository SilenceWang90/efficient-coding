package com.wp.netty.detaileddemo.client;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author wangpeng
 * @description ClientHandler
 * @date 2025/8/22 17:51
 **/
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private int i = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (i == 0) {
                ByteBuf buf = (ByteBuf) msg;
                byte[] req = new byte[buf.readableBytes()];
                buf.readBytes(req);

                String body = new String(req, "utf-8");
                String response = "收到服务器端的返回信息：" + body;
                System.out.println("client端获取server端的返回内容：" + response);
                ctx.writeAndFlush(Unpooled.copiedBuffer("今年dota2必然TI夺冠".getBytes()));
            } else {
                System.out.println("客户端已经做过回复，不再做应答");
            }
            i++;
        } finally {
            // 释放 Netty 的引用计数对象（如 ByteBuf）的内存资源，防止内存泄漏。并不释放连接和消息内容！
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
    }
}

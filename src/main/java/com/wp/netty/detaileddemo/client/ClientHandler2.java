package com.wp.netty.detaileddemo.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author wangpeng
 * @description ClientHandler2
 * @date 2025/8/25 15:03
 **/
public class ClientHandler2 extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);

        String body = new String(req, "utf-8");
        String response = "client22222收到服务器端的返回信息：" + body;
        System.out.println("client22222端获取server端的返回内容：" + response);

        ReferenceCountUtil.release(msg);

    }

}

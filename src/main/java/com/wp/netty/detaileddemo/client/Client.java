package com.wp.netty.detaileddemo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author wangpeng
 * @description Client
 * @date 2025/8/22 17:51
 **/
public class Client {
    public static void main(String[] args) throws InterruptedException {
        //1. 创建线程组: 只需要一个线程组用于我们的实际处理（网络通信的读写）
        EventLoopGroup workGroup = new NioEventLoopGroup();

        //2. 通过Bootstrap辅助类去构造client,然后进行配置响应的配置参数
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                // 相关配置信息说明参见server中的说明即可，配置相同
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .option(ChannelOption.SO_RCVBUF, 1024 * 32)
                .option(ChannelOption.SO_SNDBUF, 1024 * 32)
                //3. 初始化ChannelInitializer
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //3.1  添加客户端业务处理类
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });
        //4. client端链接server服务
        ChannelFuture cf = bootstrap.connect("127.0.0.1", 8765)
                // 阻塞式操作，保证启动完成再返回cf。否则没有该行代码会导致下面的发送方法失败
                .sync();

        //5. 发送一条数据到服务器端
        cf.channel().writeAndFlush(Unpooled.copiedBuffer("hello netty!".getBytes()));
//
//        //6. 休眠一秒钟后再发送一条数据到服务端
//        Thread.sleep(1000);
//        cf.channel().writeAndFlush(Unpooled.copiedBuffer("hello netty again!".getBytes()));

        //7. 阻塞当前线程（通常是主线程），保持Netty服务线程的运行，直到Netty的Channel被关闭为止。必须！防止main线程退出。如果集成在其他框架比如spring时就不需要这段代码了
        cf.channel().closeFuture().sync();

        // 8、释放资源
        // 不会立即释放资源，会将当前正在处理的内容处理完成，并且不再接收新的连接请求。
        workGroup.shutdownGracefully();
    }
}

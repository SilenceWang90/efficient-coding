package com.wp.netty.simpledemo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author wangpeng
 * @description Server
 * @date 2025/8/22 17:45
 **/
public class Server {
    public static void main(String[] args) throws InterruptedException {
        //1. 创建两个线程组: 一个用于进行网络连接接受的；另一个用于我们的实际处理（网络通信的读写）
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        //2. 通过ServerBootstrap辅助类去构造server
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        //3. 进行Nio Server的基础配置
        //3.1 绑定两个线程组
        serverBootstrap.group(bossGroup, workGroup)
                //3.2 因为是server端，所以需要配置NioServerSocketChannel
                .channel(NioServerSocketChannel.class)
                //3.3 设置链接超时时间，这个配置一般在客户端配置即可，服务端不需要此配置
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                //3.4 设置TCP backlog参数 = sync队列 + accept队列
                // 你的服务器在繁忙时能暂时容纳多少“等待被处理”的连接请求，相当于线程池的阻塞队列的作用
                .option(ChannelOption.SO_BACKLOG, 1024)
                //3.5 设置配置项 通信不延迟
                // TCP默认有个“攒包”机制（Nagle算法），好比你想给朋友寄几封信，TCP会劝你：“别急，再等等，说不定你等下还要写一封，我帮你把几封信塞进一个信封里寄走，省邮费！”
                // false（默认）：开启攒包。高吞吐，但有延迟。适合传输大文件。
                // true：禁用攒包。有数据就立刻发送。低延迟，但小数据包更多。适合网络游戏、实时通信、SSH终端等需要快速响应的场景。
                .childOption(ChannelOption.TCP_NODELAY, true)
                //3.6 设置配置项 接收与发送缓存区大小，以下设置都是32KB
                // 操作系统内核控制，缓冲接收到的网络数据流，应对突发流量，防止丢包。仓库的卸货平台：快递车（网络数据）来得太快，工人们（应用程序）来不及搬，就先堆在平台上。平台大小就是 SO_RCVBUF。
                // 如果 SO_RCVBUF 满了会怎样？操作系统会通过TCP协议通知对端发送方：“我这边堵了，别发了！（TCP流量控制）”，从而从根源上降低发送速度。
                .childOption(ChannelOption.SO_RCVBUF, 1024 * 32)
                // 操作系统内核，缓冲要发送的网络数据流，让操作系统优化发送效率。仓库的装货平台：工人们生产了货物（调用write），都先搬到装货平台（flush到SO_SNDBUF），等快递车来了统一装车发送。平台大小就是 SO_SNDBUF。
                // 如果 SO_SNDBUF 满了会怎样？Netty 的 flush() 操作会阻塞（如果是异步的，则会等待），直到内核缓冲区有空间接收新的数据。这也会反过来导致 Netty 自身的发送队列积压。
                .childOption(ChannelOption.SO_SNDBUF, 1024 * 32)
                //3.7 进行初始化 ChannelInitializer , 用于构建双向链表 "pipeline" 添加业务handler处理
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //3.8 这里仅仅只是添加一个业务处理器：ServerHandler（后面我们要针对他进行编码）
                        ch.pipeline().addLast(new ServerHandler());
                    }
                });

        //4. 服务器端绑定端口并启动服务
        ChannelFuture cf = serverBootstrap.bind(8765).sync();
        // 保持服务器运行。必须！防止main线程退出。如果集成在其他框架比如spring时就不需要这段代码了
        cf.channel().closeFuture().sync();

        //5. 释放资源
        // 不会立即释放资源，会将当前正在处理的内容处理完成，并且不再接收新的连接请求。
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}

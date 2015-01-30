package com.github.rd.jlv.io;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import com.google.common.base.Preconditions;

public class LogServer {

	private int port;

	public LogServer(int port) {
		Preconditions.checkArgument(port > 0, "Port value must be a positive number: %s", port);
		this.port = port;
	}

	public void run() {
		EventLoopGroup masterGroup = new OioEventLoopGroup(1);
		EventLoopGroup workerGroup = new OioEventLoopGroup();

		try {
			ServerBootstrap boot = new ServerBootstrap();
			boot.group(masterGroup, workerGroup)
					.channel(OioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(
									new ObjectEncoder(),
									new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
									new LogServerHandler());
						}
					});
			boot.bind(port).sync().channel().closeFuture().sync();
		} finally {
			masterGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}

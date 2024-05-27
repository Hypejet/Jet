package net.hypejet.jet.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.hypejet.jet.protocol.packet.serverbound.ServerBoundPacketRegistry;
import net.hypejet.jet.server.network.InboundManager;
import net.hypejet.jet.server.network.NetworkManager;
import net.hypejet.jet.server.network.PacketDecoder;
import net.hypejet.jet.server.protocol.JetServerBoundPacketRegistry;

public class Main {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBoundPacketRegistry serverBoundPacketRegistry = new JetServerBoundPacketRegistry();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            NetworkManager networkManager = new NetworkManager();
                            ch.pipeline()
                                    .addLast(new PacketDecoder(networkManager, serverBoundPacketRegistry))
                                    .addLast(new InboundManager(networkManager));
                        }
                    });

            ChannelFuture future = bootstrap.bind(25565).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
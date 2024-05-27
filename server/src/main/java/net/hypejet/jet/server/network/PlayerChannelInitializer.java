package net.hypejet.jet.server.network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import net.hypejet.jet.protocol.packet.serverbound.ServerBoundPacketRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PlayerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final ServerBoundPacketRegistry serverBoundPacketRegistry;

    public PlayerChannelInitializer(@NonNull ServerBoundPacketRegistry serverBoundPacketRegistry) {
        this.serverBoundPacketRegistry = serverBoundPacketRegistry;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        JetPlayerConnection playerConnection = new JetPlayerConnection();
        ch.pipeline()
                .addLast(new PacketDecoder(playerConnection, this.serverBoundPacketRegistry))
                .addLast(new InboundManager(playerConnection));
    }
}
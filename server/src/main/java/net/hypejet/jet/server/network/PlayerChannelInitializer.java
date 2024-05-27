package net.hypejet.jet.server.network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import net.hypejet.jet.protocol.packet.serverbound.ServerBoundPacketRegistry;
import net.hypejet.jet.server.network.serialization.PacketDecoder;
import net.hypejet.jet.server.network.serialization.PacketEncoder;
import net.hypejet.jet.server.network.serialization.PacketLengthEncoder;
import net.hypejet.jet.server.player.JetPlayerConnection;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PlayerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final ServerBoundPacketRegistry serverBoundPacketRegistry;

    public PlayerChannelInitializer(@NonNull ServerBoundPacketRegistry serverBoundPacketRegistry) {
        this.serverBoundPacketRegistry = serverBoundPacketRegistry;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        JetPlayerConnection playerConnection = new JetPlayerConnection(ch);
        ch.pipeline()
                .addFirst(new PacketEncoder())
                .addFirst(new PacketLengthEncoder())
                .addFirst(new PacketReader(playerConnection))
                .addFirst(new PacketDecoder(playerConnection, this.serverBoundPacketRegistry));
    }
}
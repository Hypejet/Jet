package net.hypejet.jet.server.network.netty.reader;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.hypejet.jet.acquisition.Acquisition;
import net.hypejet.jet.event.events.packet.PacketReceiveEvent;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.server.network.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.Session;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ChannelInboundHandlerAdapter channel inbound handler adapter}, which processes
 * Minecraft {@linkplain  ClientPacket client packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public final class PacketReader extends ChannelInboundHandlerAdapter {

    private final SocketPlayerConnection playerConnection;

    /**
     * Constructs a {@linkplain PacketReader packet reader}.
     *
     * @param playerConnection a player connection to read packets for
     * @since 1.0
     */
    public PacketReader(@NonNull SocketPlayerConnection playerConnection) {
        this.playerConnection = playerConnection;
    }

    @Override
    public void channelRead(@NonNull ChannelHandlerContext ctx, @NonNull Object msg) {
        if (!(msg instanceof UnhandledPacket<?> unhandledPacket))
            throw new IllegalStateException("A message received is not an unhandled client packet");
        this.handlePacket(unhandledPacket); // Handle the packet with generics
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.playerConnection.uncaughtException(Thread.currentThread(), cause);
    }

    private <P extends ClientPacket> void handlePacket(@NonNull UnhandledPacket<P> unhandledPacket) {
        try (Acquisition<Session> sessionAcquisition = this.playerConnection.createOrReuseSessionAcquisition()) {
            P packet = unhandledPacket.packet();
            ClientPacketHandler<P> handler = unhandledPacket.handler();

            PacketReceiveEvent event = new PacketReceiveEvent(packet);
            this.playerConnection.server().eventNode().call(event);
            if (event.isCancelled()) return;

            handler.handle(packet, sessionAcquisition.get().sessionTask());
        }
    }
}
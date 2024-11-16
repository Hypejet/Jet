package net.hypejet.jet.server.network.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.hypejet.jet.acquisition.Acquisition;
import net.hypejet.jet.event.events.packet.PacketReceiveEvent;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.server.network.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketRegistry;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.task.SessionTask;
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
        if (!(msg instanceof ClientPacket packet))
            throw new IllegalStateException("A message received is not a client packet");

        try (Acquisition<Session> sessionAcquisition = this.playerConnection.createOrReuseSessionAcquisition()) {
            PacketReceiveEvent event = new PacketReceiveEvent(packet);
            this.playerConnection.server().eventNode().call(event);
            if (event.isCancelled()) return;

            ClientPacketCodec<?> codec = ClientPacketRegistry.codec(packet.getClass());
            if (codec == null) throw packetReaderNotFound(packet);

            // Handle the packet with java generics
            handlePacket(codec, packet, sessionAcquisition.get().sessionTask());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.playerConnection.uncaughtException(Thread.currentThread(), cause);
    }

    private static <P extends ClientPacket> void handlePacket(@NonNull ClientPacketCodec<P> codec,
                                                              @NonNull ClientPacket packet,
                                                              @NonNull SessionTask sessionTask) {
        codec.handle(codec.getPacketClass().cast(packet), sessionTask);
    }

    private static @NonNull IllegalArgumentException packetReaderNotFound(@NonNull ClientPacket packet) {
        return new IllegalArgumentException("Could not find a client packet reader for packet: "
                + packet.getClass().getSimpleName());
    }
}
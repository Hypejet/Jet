package net.hypejet.jet.server.network.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.hypejet.jet.event.events.packet.PacketReceiveEvent;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketRegistry;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a {@linkplain ChannelInboundHandlerAdapter channel inbound handler adapter}, which processes
 * Minecraft {@linkplain  ClientPacket client packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public final class PacketReader extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketReader.class);

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
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!this.playerConnection.getChannel().isActive()) return; // The connection was closed

        if (!(msg instanceof ClientPacket packet))
            throw new IllegalStateException("A message received is not a client packet");

        PacketReceiveEvent event = new PacketReceiveEvent(packet);
        this.playerConnection.server().eventNode().call(event);
        if (event.isCancelled()) return;

        ClientPacketCodec<?> codec = ClientPacketRegistry.codec(packet.getClass());
        if (codec == null) throw packetReaderNotFound(packet);

        handlePacket(codec, packet, this.playerConnection); // Handle the packet with java generics
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.playerConnection.close(); // Close the connection to avoid more issues
        LOGGER.error("An error occurred while reading a packet", cause);
    }

    private static <P extends ClientPacket> void handlePacket(@NonNull ClientPacketCodec<P> codec,
                                                              @NonNull ClientPacket packet,
                                                              @NonNull SocketPlayerConnection connection) {
        codec.handle(codec.getPacketClass().cast(packet), connection);
    }

    private static @NonNull IllegalArgumentException packetReaderNotFound(@NonNull ClientPacket packet) {
        return new IllegalArgumentException("Could not find a client packet reader for packet: "
                + packet.getClass().getSimpleName());
    }
}
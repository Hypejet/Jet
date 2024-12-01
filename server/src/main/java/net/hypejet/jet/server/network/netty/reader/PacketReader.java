package net.hypejet.jet.server.network.netty.reader;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.hypejet.jet.acquisition.Acquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.packet.client.ClientPacket;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.packet.client.ClientPacketRegistry;
import net.hypejet.jet.server.network.packet.client.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.session.Session;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents {@linkplain ChannelInboundHandlerAdapter a channel inbound handler adapter}, which
 * handles {@linkplain ClientPacket client packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public final class PacketReader extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketReader.class);

    private final SocketPlayerConnection connection;

    /**
     * Constructs the {@linkplain PacketReader packet reader}.
     *
     * @param connection a player connection to read packets for
     * @since 1.0
     */
    public PacketReader(@NonNull SocketPlayerConnection connection) {
        this.connection = NullabilityUtil.requireNonNull(connection, "connection");
    }

    @Override
    public void channelRead(@NonNull ChannelHandlerContext ctx, @NonNull Object msg) {
        try (Acquisition<Session> sessionAcquisition = this.connection.createSessionAcquisition()) {
            if (!(msg instanceof ClientPacket packet))
                throw new IllegalStateException("A message received is not a client packet");

            Class<? extends ClientPacket> packetClass = packet.getClass();
            ClientPacketHandler<?> handler = ClientPacketRegistry.handlerFor(packetClass);

            if (handler == null) {
                LOGGER.warn("No packet handler was specified for a client packet with class of {}",
                        packetClass.getSimpleName());
                return;
            }

            handlePacket(packet, sessionAcquisition.get(), handler); // Handle the packet with generics
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.connection.uncaughtException(Thread.currentThread(), cause);
    }

    private static <P extends ClientPacket> void handlePacket(@NonNull ClientPacket packet, @NonNull Session session,
                                                              @NonNull ClientPacketHandler<P> handler) {
        handler.handle(handler.packetClass().cast(packet), session);
    }
}
package net.hypejet.jet.server.network.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Objects;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.packet.RawPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain SimpleChannelInboundHandler a simple channel inbound handler}, which
 * handles {@linkplain RawPacket raw packets} and adds them to a queue of incoming packets.
 *
 * @since 1.0
 */
public final class RawPacketHandler extends SimpleChannelInboundHandler<RawPacket> {

    private final SocketPlayerConnection connection;

    /**
     * Constructs the {@linkplain RawPacketHandler packet reader}.
     *
     * @param connection a player connection to read packets for
     * @since 1.0
     */
    public RawPacketHandler(@NonNull SocketPlayerConnection connection) {
        super(RawPacket.class);
        this.connection = Objects.requireNonNull(connection, "connection");
    }

    @Override
    public void channelRead0(@NonNull ChannelHandlerContext ctx, @NonNull RawPacket msg) {
        this.connection.clientPacketReader().queue(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.connection.uncaughtException(Thread.currentThread(), cause);
    }
}
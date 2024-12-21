package net.hypejet.jet.server.network.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.hypejet.concurrency.object.ObjectAcquirable;
import net.hypejet.concurrency.object.ObjectAcquisition;
import net.hypejet.concurrency.object.WriteObjectAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacketRegistry;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacketRegistry.RegistryPacketSpecification;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.SessionUpdateHandler;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

/**
 * Represents {@linkplain MessageToByteEncoder a message-to-byte encoder}, which encodes {@linkplain ServerPacket
 * server packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 * @see MessageToByteEncoder
 */
public final class PacketEncoder extends MessageToByteEncoder<ServerPacket> implements SessionUpdateHandler {

    private final SocketPlayerConnection connection;
    // More about existence of this field in a comment in SocketPlayerConnection.WriteSessionAcquisition#set
    private final ObjectAcquirable<ProtocolState> protocolState;

    /**
     * Constructs the {@linkplain PacketEncoder packet encoder}.
     *
     * @param connection a connection that the encoding should be handled for
     * @param initialProtocolState an initial protocol state that the encoder should use
     * @since 1.0
     */
    public PacketEncoder(@NonNull SocketPlayerConnection connection, @NonNull ProtocolState initialProtocolState) {
        this.connection = NullabilityUtil.requireNonNull(connection, "connection");
        NullabilityUtil.requireNonNull(initialProtocolState, "initial protocol state");
        this.protocolState = new ObjectAcquirable<>(initialProtocolState);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ServerPacket msg, ByteBuf out) {
        try (ObjectAcquisition<ProtocolState> protocolStateAcquisition = this.protocolState.acquireRead()) {
            ProtocolState state = protocolStateAcquisition.get();
            Class<? extends ServerPacket> packetClass = msg.getClass();

            RegistryPacketSpecification<?> specification = ServerPacketRegistry.specificationFor(state, packetClass);
            if (specification == null) {
                String name = packetClass.getSimpleName();
                throw new IllegalArgumentException(String.format("Could not find a packet codec for packet %s", name));
            }

            write(specification, out, msg); // Write the packet with java generics
        } catch (Throwable throwable) {
            this.connection.uncaughtException(Thread.currentThread(), throwable);
        }
    }

    @Override
    public void handleSessionUpdate(@NotNull Session newSession) {
        try (WriteObjectAcquisition<ProtocolState> protocolStateAcquisition = this.protocolState.acquireWrite()) {
            protocolStateAcquisition.set(newSession.protocolState());
        }
    }

    private static <P extends ServerPacket> void write(@NonNull RegistryPacketSpecification<P> specification,
                                                       @NonNull ByteBuf buf, @NonNull ServerPacket packet) {
        VarIntNetworkCodec.INSTANCE.write(buf, specification.packetIdentifier());
        specification.packetWriter().write(buf, specification.packetClass().cast(packet));
    }
}
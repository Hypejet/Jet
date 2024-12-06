package net.hypejet.jet.server.network.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.hypejet.jet.acquisition.Acquisition;
import net.hypejet.jet.acquisition.MutableAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.ProtocolState;
import net.hypejet.jet.network.packet.server.ServerPacket;
import net.hypejet.jet.server.acquisition.value.AcquirableValue;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.server.ServerPacketRegistry;
import net.hypejet.jet.server.network.packet.server.ServerPacketRegistry.RegistryPacketSpecification;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain MessageToByteEncoder a message-to-byte encoder}, which encodes {@linkplain ServerPacket
 * server packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 * @see MessageToByteEncoder
 */
public final class PacketEncoder extends MessageToByteEncoder<ServerPacket> {

    private final SocketPlayerConnection connection;
    // More about existence of this field in a comment in SocketPlayerConnection.SessionAcquisition#onSet
    private final AcquirableValue<ProtocolState> protocolState;

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
        this.protocolState = new AcquirableValue<>(initialProtocolState);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ServerPacket msg, ByteBuf out) {
        try (Acquisition<ProtocolState> protocolStateAcquisition = this.protocolState.acquire()) {
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

    private static <P extends ServerPacket> void write(@NonNull RegistryPacketSpecification<P> specification,
                                                       @NonNull ByteBuf buf, @NonNull ServerPacket packet) {
        VarIntNetworkCodec.INSTANCE.write(buf, specification.packetIdentifier());
        specification.packetWriter().write(buf, specification.packetClass().cast(packet));
    }

    /**
     * Updates {@linkplain ProtocolState a protocol state} that is used by this packet encoder.
     *
     * @param protocolState the protocol state
     * @since 1.0
     */
    public void updateProtocolState(@NonNull ProtocolState protocolState) {
        try (MutableAcquisition<ProtocolState> protocolStateAcquisition = this.protocolState.acquireMutable()) {
            protocolStateAcquisition.set(protocolState);
        }
    }
}
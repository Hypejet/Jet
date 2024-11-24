package net.hypejet.jet.server.network.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.hypejet.jet.acquisition.Acquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.server.network.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketRegistry;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketRegistry.RegistryPacketSpecification;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain MessageToByteEncoder message-to-byte encoder}, which encodes {@linkplain ServerPacket
 * server packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 * @see MessageToByteEncoder
 */
public final class PacketEncoder extends MessageToByteEncoder<ServerPacket> {

    private final SocketPlayerConnection connection;
    private final ServerPacketRegistry packetRegistry;

    /**
     * Constructs the {@linkplain PacketEncoder packet encoder}.
     *
     * @param connection a connection that the encoding should be handled for
     * @param packetRegistry a client packet registry that should be used for finding client packet handlers
     * @since 1.0
     */
    public PacketEncoder(@NonNull SocketPlayerConnection connection, @NonNull ServerPacketRegistry packetRegistry) {
        this.connection = NullabilityUtil.requireNonNull(connection, "connection");
        this.packetRegistry = NullabilityUtil.requireNonNull(packetRegistry, "packet registry");
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ServerPacket msg, ByteBuf out) {
        try (Acquisition<ProtocolState> protocolStateAcquisition = this.connection.protocolState()) {
            ProtocolState state = protocolStateAcquisition.get();
            Class<? extends ServerPacket> packetClass = msg.getClass();

            RegistryPacketSpecification<?> specification = this.packetRegistry.specificationFor(state, packetClass);
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
}
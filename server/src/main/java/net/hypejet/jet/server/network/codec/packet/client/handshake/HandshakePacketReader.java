package net.hypejet.jet.server.network.codec.packet.client.handshake;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.index.IndexNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.handshake.ClientHandshakePacket;
import net.hypejet.jet.server.network.packet.packets.client.handshake.ClientHandshakePacket.HandshakeIntent;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.index.IndexUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * Represents {@linkplain NetworkReader a network reader},
 * which reads {@link ClientHandshakePacket a handshake packet}.
 *
 * @since 1.0
 * @see ClientHandshakePacket
 * @see NetworkReader
 */
public final class HandshakePacketReader implements NetworkReader<ClientHandshakePacket> {
    /**
     * An instance of the {@linkplain HandshakePacketReader handshake packet reader}.
     *
     * @since 1.0
     */
    public static final HandshakePacketReader INSTANCE = new HandshakePacketReader();

    private static final IndexNetworkCodec<HandshakeIntent, Integer> INTENT_CODEC = new IndexNetworkCodec<>(
            IndexUtil.fromMap(Map.of(
                    1, HandshakeIntent.STATUS,
                    2, HandshakeIntent.LOGIN,
                    3, HandshakeIntent.TRANSFER
            )),
            VarIntNetworkCodec.INSTANCE
    );

    private static final StringNetworkCodec ADDRESS_CODEC = StringNetworkCodec.create(255);

    private HandshakePacketReader() {}

    @Override
    public @NonNull ClientHandshakePacket read(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager) {
        return new ClientHandshakePacket(
                VarIntNetworkCodec.INSTANCE.read(buf, registryManager), ADDRESS_CODEC.read(buf, registryManager),
                buf.readUnsignedShort(), INTENT_CODEC.read(buf, registryManager)
        );
    }
}
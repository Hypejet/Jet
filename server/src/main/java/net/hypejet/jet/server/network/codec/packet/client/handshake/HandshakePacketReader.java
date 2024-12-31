package net.hypejet.jet.server.network.codec.packet.client.handshake;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.mapper.MapperNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.handshake.ClientHandshakePacket;
import net.hypejet.jet.server.network.packet.packets.client.handshake.ClientHandshakePacket.HandshakeIntent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@link NetworkReader a network reader}, which reads {@link ClientHandshakePacket a handshake packet}.
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

    private static final MapperNetworkCodec<HandshakeIntent, Integer> INTENT_CODEC = new MapperNetworkCodec<>(
            Mapper.builder(HandshakeIntent.class, int.class)
                    .register(HandshakeIntent.STATUS, 1)
                    .register(HandshakeIntent.LOGIN, 2)
                    .register(HandshakeIntent.TRANSFER, 3)
                    .build(),
            VarIntNetworkCodec.INSTANCE
    );

    private static final StringNetworkCodec ADDRESS_CODEC = StringNetworkCodec.create(255);

    private HandshakePacketReader() {}

    @Override
    public @NonNull ClientHandshakePacket read(@NonNull ByteBuf buf) {
        return new ClientHandshakePacket(
                VarIntNetworkCodec.INSTANCE.read(buf), ADDRESS_CODEC.read(buf),
                buf.readUnsignedShort(), INTENT_CODEC.read(buf)
        );
    }
}
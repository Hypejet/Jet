package net.hypejet.jet.server.network.protocol.packet.client.codec.handshake;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.handshake.ClientHandshakePacket;
import net.hypejet.jet.protocol.packet.client.handshake.ClientHandshakePacket.HandshakeIntent;
import net.hypejet.jet.server.network.protocol.codecs.enums.EnumVarIntCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.session.JetHandshakeSession;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.EnumMap;

/**
 * Represents a {@link ClientPacketCodec client packet codec}, which reads and writes a {@link ClientHandshakePacket
 * handshake packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientHandshakePacket
 * @see ClientPacketCodec
 */
public final class HandshakePacketCodec extends ClientPacketCodec<ClientHandshakePacket> {

    private static final int MAX_ADDRESS_LENGTH = 255;

    private static final EnumVarIntCodec<HandshakeIntent> handshakeIntentCodec = EnumVarIntCodec
            .builder(HandshakeIntent.class)
            .add(HandshakeIntent.STATUS, 1)
            .add(HandshakeIntent.LOGIN, 2)
            .add(HandshakeIntent.TRANSFER, 3)
            .build();

    /**
     * Constructs the {@linkplain HandshakePacketCodec handshake packet codec}.
     *
     * @since 1.0
     */
    public HandshakePacketCodec() {
        super(ClientPacketIdentifiers.HANDSHAKE, ClientHandshakePacket.class);
    }

    @Override
    public @NonNull ClientHandshakePacket read(@NonNull ByteBuf buf) {
        return new ClientHandshakePacket(NetworkUtil.readVarInt(buf), NetworkUtil.readString(buf, MAX_ADDRESS_LENGTH),
                buf.readUnsignedShort(), handshakeIntentCodec.read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientHandshakePacket object) {
        NetworkUtil.writeVarInt(buf, object.protocolVersion());
        NetworkUtil.writeString(buf, object.serverAddress(), MAX_ADDRESS_LENGTH);
        buf.writeShort(object.serverPort());
        handshakeIntentCodec.write(buf, object.intent());
    }

    @Override
    public void handle(@NonNull ClientHandshakePacket packet, @NonNull SocketPlayerConnection connection) {
        if (!(connection.getSession() instanceof JetHandshakeSession session)) {
            throw new IllegalStateException("Received a handshake packet while the current session is not " +
                    "a handshake session");
        }
        session.handleHandshakePacket(packet);
    }
}
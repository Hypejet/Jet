package net.hypejet.jet.server.network.protocol.packet.client.codec.handshake;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.handshake.ClientHandshakePacket;
import net.hypejet.jet.protocol.packet.client.handshake.ClientHandshakePacket.HandshakeIntent;
import net.hypejet.jet.server.network.protocol.codecs.enums.EnumVarIntCodec;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.session.JetHandshakeSession;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

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

    private static final EnumVarIntCodec<HandshakeIntent> HANDSHAKE_INTENT_CODEC = EnumVarIntCodec
            .builder(HandshakeIntent.class)
            .add(HandshakeIntent.STATUS, 1)
            .add(HandshakeIntent.LOGIN, 2)
            .add(HandshakeIntent.TRANSFER, 3)
            .build();

    private static final StringNetworkCodec ADDRESS_CODEC = StringNetworkCodec.create(255);

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
        return new ClientHandshakePacket(VarIntNetworkCodec.instance().read(buf), ADDRESS_CODEC.read(buf),
                buf.readUnsignedShort(), HANDSHAKE_INTENT_CODEC.read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientHandshakePacket object) {
        VarIntNetworkCodec.instance().write(buf, object.protocolVersion());
        ADDRESS_CODEC.write(buf, object.serverAddress());
        buf.writeShort(object.serverPort());
        HANDSHAKE_INTENT_CODEC.write(buf, object.intent());
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
package net.hypejet.jet.server.network.protocol.packet.client.handler.handshake;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.handshake.ClientHandshakePacket;
import net.hypejet.jet.protocol.packet.client.handshake.ClientHandshakePacket.HandshakeIntent;
import net.hypejet.jet.server.network.protocol.codecs.enums.EnumVarIntCodec;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.HandshakeTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@link ClientPacketHandler a client packet handler}, which reads and handles
 * {@link ClientHandshakePacket a handshake packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientHandshakePacket
 * @see ClientPacketHandler
 */
public final class HandshakePacketHandler implements ClientPacketHandler<ClientHandshakePacket> {

    private static final EnumVarIntCodec<HandshakeIntent> HANDSHAKE_INTENT_CODEC = EnumVarIntCodec
            .builder(HandshakeIntent.class)
            .add(HandshakeIntent.STATUS, 1)
            .add(HandshakeIntent.LOGIN, 2)
            .add(HandshakeIntent.TRANSFER, 3)
            .build();

    private static final StringNetworkCodec ADDRESS_CODEC = StringNetworkCodec.create(255);

    @Override
    public @NonNull ClientHandshakePacket read(@NonNull ByteBuf buf) {
        return new ClientHandshakePacket(VarIntNetworkCodec.instance().read(buf), ADDRESS_CODEC.read(buf),
                buf.readUnsignedShort(), HANDSHAKE_INTENT_CODEC.read(buf));
    }

    @Override
    public void handle(@NonNull ClientHandshakePacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof HandshakeTask handshakeTask))
            throw new IllegalArgumentException("The current session task must be a handshaking task");
        handshakeTask.handleHandshakePacket(packet);
    }
}
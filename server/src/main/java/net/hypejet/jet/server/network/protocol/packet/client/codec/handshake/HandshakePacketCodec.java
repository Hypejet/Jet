package net.hypejet.jet.server.network.protocol.packet.client.codec.handshake;

import io.netty.buffer.ByteBuf;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.protocol.packet.client.handshake.ClientHandshakePacket;
import net.hypejet.jet.protocol.packet.client.handshake.ClientHandshakePacket.HandshakeIntent;
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

    private static final IntObjectMap<HandshakeIntent> idToIntentMap = new IntObjectHashMap<>();
    private static final EnumMap<HandshakeIntent, Integer> intentToIdMap = new EnumMap<>(HandshakeIntent.class);

    static {
        idToIntentMap.put(1, HandshakeIntent.STATUS);
        idToIntentMap.put(2, HandshakeIntent.LOGIN);
        idToIntentMap.put(3, HandshakeIntent.TRANSFER);
        idToIntentMap.forEach((id, intent) -> intentToIdMap.put(intent, id));
    }

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
        int protocolVersion = NetworkUtil.readVarInt(buf);
        String serverAddress = NetworkUtil.readString(buf);
        int serverPort = buf.readUnsignedShort();

        int intentIdentifier = NetworkUtil.readVarInt(buf);
        HandshakeIntent intent = idToIntentMap.get(intentIdentifier);

        if (intent == null) {
            throw new IllegalArgumentException("Unknown handshake intent: "+ intentIdentifier);
        }

        return new ClientHandshakePacket(protocolVersion, serverAddress, serverPort, intent);
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientHandshakePacket object) {
        NetworkUtil.writeVarInt(buf, object.protocolVersion());
        NetworkUtil.writeString(buf, object.serverAddress());
        buf.writeShort(object.serverPort());
        NetworkUtil.writeVarInt(buf, intentToIdMap.get(object.intent()));
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
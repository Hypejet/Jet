package net.hypejet.jet.server.network.protocol.listener.login;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.login.ClientCookieResponsePacket;
import net.hypejet.jet.server.network.buffer.NetworkBuffer;
import net.hypejet.jet.server.network.protocol.listener.PacketReader;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a {@linkplain PacketReader packet reader}, which reads a {@linkplain ClientCookieResponsePacket cookie
 * response packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientCookieResponsePacket
 * @see PacketReader
 */
public final class ClientCookieResponsePacketReader extends PacketReader<ClientCookieResponsePacket> {

    /**
     * Constructs a {@link ClientCookieResponsePacketReader cookie response packet reader}.
     *
     * @since 1.0
     */
    public ClientCookieResponsePacketReader() {
        super(4, ProtocolState.LOGIN);
    }

    @Override
    public @NonNull ClientCookieResponsePacket read(@NonNull NetworkBuffer buffer) {
        return new ClientCookieResponsePacketImpl(
                buffer.readIdentifier(),
                buffer.readBoolean() ? buffer.readByteArray() : null
        );
    }

    /**
     * Represents an implementation of {@link ClientCookieResponsePacket cookie response packet}.
     *
     * @param identifier an identifier of the cookie
     * @param data an optional data of the cookie
     * @since 1.0
     * @author Codestech
     * @see ClientCookieResponsePacket
     */
    private record ClientCookieResponsePacketImpl(@NonNull Key identifier, byte @Nullable [] data)
            implements ClientCookieResponsePacket {}
}

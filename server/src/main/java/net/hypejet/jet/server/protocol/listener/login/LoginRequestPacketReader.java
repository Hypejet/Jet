package net.hypejet.jet.server.protocol.listener.login;

import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.server.protocol.listener.PacketReader;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginRequestPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents a {@link PacketReader packet reader}, which reads a {@link ClientLoginRequestPacket login request packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientLoginRequestPacket
 * @see PacketReader
 */
public final class LoginRequestPacketReader extends PacketReader<ClientLoginRequestPacket> {

    /**
     * Constructs a {@link LoginRequestPacketReader login request packet reader}.
     *
     * @since 1.0
     */
    public LoginRequestPacketReader() {
        super(0, ProtocolState.LOGIN);
    }

    @Override
    public @NonNull ClientLoginRequestPacket read(@NonNull NetworkBuffer buffer) {
        return new ClientLoginRequestPacketImpl(buffer.readString(), buffer.readUniqueId());
    }

    /**
     * Represents an implementation of {@link ClientLoginRequestPacket login request packet}.
     *
     * @param username a username of a player that sends the packet
     * @param uniqueId a {@link UUID unique identifier} of a player that sends the packet
     * @since 1.0
     * @see ClientLoginRequestPacket
     */
    private record ClientLoginRequestPacketImpl(@NonNull String username,
                                                @NonNull UUID uniqueId) implements ClientLoginRequestPacket {}
}
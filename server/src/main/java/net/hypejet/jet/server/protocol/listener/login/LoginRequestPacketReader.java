package net.hypejet.jet.server.protocol.listener.login;

import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.server.protocol.PacketReader;
import net.hypejet.jet.protocol.packet.serverbound.login.LoginRequestPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents a {@link PacketReader packet reader}, which reads a {@link LoginRequestPacket login request packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see LoginRequestPacket
 */
public final class LoginRequestPacketReader extends PacketReader<LoginRequestPacket> {

    /**
     * Constructs a {@link LoginRequestPacketReader login request packet reader}.
     *
     * @since 1.0
     */
    public LoginRequestPacketReader() {
        super(0, ProtocolState.LOGIN);
    }

    @Override
    public @NonNull LoginRequestPacket read(@NonNull NetworkBuffer buffer) {
        return new LoginRequestPacketImpl(buffer.readString(), buffer.readUniqueId());
    }

    /**
     * Represents an implementation of {@link LoginRequestPacket login request packet}.
     *
     * @param username a username of a player that sends the packet
     * @param uniqueId a {@link UUID unique identifier} of a player that sends the packet
     * @since 1.0
     */
    private record LoginRequestPacketImpl(@NonNull String username,
                                          @NonNull UUID uniqueId) implements LoginRequestPacket {}
}
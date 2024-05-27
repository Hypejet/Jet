package net.hypejet.jet.server.protocol;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.serverbound.PacketReader;
import net.hypejet.jet.protocol.packet.serverbound.ServerBoundPacketRegistry;
import net.hypejet.jet.server.protocol.packet.serverbound.handshake.HandshakePacketReader;
import net.hypejet.jet.server.protocol.packet.serverbound.login.LoginRequestPacketReader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Set;

public final class JetServerBoundPacketRegistry implements ServerBoundPacketRegistry {

    private final Set<PacketReader<?>> packetReaders = Set.of(
            new HandshakePacketReader(),
            new LoginRequestPacketReader()
    );

    @Override
    public @Nullable PacketReader<?> getReader(int packetId, @NonNull ProtocolState state) {
        for (PacketReader<?> packetReader : this.packetReaders) {
            if (packetReader.getPacketId() != packetId || packetReader.getProtocolState() != state) continue;
            return packetReader;
        }
        return null;
    }
}
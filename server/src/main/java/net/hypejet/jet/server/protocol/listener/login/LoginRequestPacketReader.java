package net.hypejet.jet.server.protocol.listener.login;

import net.hypejet.jet.buffer.ReadOnlyNetworkBuffer;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.serverbound.PacketReader;
import net.hypejet.jet.protocol.packet.serverbound.login.LoginRequestPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class LoginRequestPacketReader extends PacketReader<LoginRequestPacket> {

    public LoginRequestPacketReader() {
        super(0, ProtocolState.LOGIN);
    }

    @Override
    public @NonNull LoginRequestPacket read(@NonNull ReadOnlyNetworkBuffer buffer) {
        return new LoginRequestPacket(buffer.readString(), buffer.readUniqueId());
    }
}
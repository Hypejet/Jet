package net.hypejet.jet.server.protocol.packet.serverbound.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.serverbound.PacketReader;
import net.hypejet.jet.protocol.packet.serverbound.login.LoginRequestPacket;
import net.hypejet.jet.server.network.PacketDecoder;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class LoginRequestPacketReader extends PacketReader<LoginRequestPacket> {

    public LoginRequestPacketReader() {
        super(0, ProtocolState.LOGIN);
    }

    @Override
    public @NonNull LoginRequestPacket read(@NonNull ByteBuf buffer) {
        return new LoginRequestPacket(
                PacketDecoder.readString(buffer),
                PacketDecoder.readUUID(buffer)
        );
    }
}
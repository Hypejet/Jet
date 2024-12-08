package net.hypejet.jet.server.network.codec.packet.client.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.packet.packets.client.login.ClientLoginRequestLoginPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.codec.other.UUIDNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@link NetworkReader a network reader}, which reads {@link ClientLoginRequestLoginPacket a login request
 * login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientLoginRequestLoginPacket
 * @see NetworkReader
 */
public final class ClientLoginRequestLoginPacketReader implements NetworkReader<ClientLoginRequestLoginPacket> {
    @Override
    public @NonNull ClientLoginRequestLoginPacket read(@NonNull ByteBuf buf) {
        return new ClientLoginRequestLoginPacket(
                StringNetworkCodec.MAX_16_INSTANCE.read(buf),
                UUIDNetworkCodec.INSTANCE.read(buf)
        );
    }
}
package net.hypejet.jet.server.network.codec.packet.client.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.packet.packets.client.login.ClientEncryptionResponseLoginPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.aggregate.array.bytes.ByteArrayNetworkReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@link NetworkReader a network reader}, which reads
 * {@link ClientEncryptionResponseLoginPacket an encryption response login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientEncryptionResponseLoginPacket
 * @see NetworkReader
 */
public final class ClientEncryptionResponseLoginPacketReader
        implements NetworkReader<ClientEncryptionResponseLoginPacket> {
    @Override
    public @NonNull ClientEncryptionResponseLoginPacket read(@NonNull ByteBuf buf) {
        return new ClientEncryptionResponseLoginPacket(
                ByteArrayNetworkReader.INSTANCE.read(buf),
                ByteArrayNetworkReader.INSTANCE.read(buf)
        );
    }
}
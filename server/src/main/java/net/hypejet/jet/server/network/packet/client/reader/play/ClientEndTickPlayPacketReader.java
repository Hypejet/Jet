package net.hypejet.jet.server.network.packet.client.reader.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.client.play.ClientEndTickPlayPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads {@linkplain ClientEndTickPlayPacket a client
 * end tick play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientEndTickPlayPacket
 * @see NetworkReader
 */
public final class ClientEndTickPlayPacketReader implements NetworkReader<ClientEndTickPlayPacket> {

    private static final ClientEndTickPlayPacket PACKET = new ClientEndTickPlayPacket();

    @Override
    public @NonNull ClientEndTickPlayPacket read(@NonNull ByteBuf buf) {
        return PACKET;
    }
}
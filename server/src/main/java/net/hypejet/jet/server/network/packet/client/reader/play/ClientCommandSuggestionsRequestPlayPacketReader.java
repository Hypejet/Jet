package net.hypejet.jet.server.network.packet.client.reader.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.client.play.ClientCommandSuggestionsRequestPlayPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientCommandSuggestionsRequestPlayPacket a command suggestions request play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientCommandSuggestionsRequestPlayPacket
 * @see NetworkReader
 */
public final class ClientCommandSuggestionsRequestPlayPacketReader
        implements NetworkReader<ClientCommandSuggestionsRequestPlayPacket> {

    private static final StringNetworkCodec TEXT_CODEC = StringNetworkCodec.create(32_500);

    @Override
    public @NonNull ClientCommandSuggestionsRequestPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientCommandSuggestionsRequestPlayPacket(
                VarIntNetworkCodec.INSTANCE.read(buf),
                TEXT_CODEC.read(buf)
        );
    }
}
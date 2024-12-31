package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientCommandSuggestionsRequestPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientCommandSuggestionsRequestPlayPacket a command suggestions request play packet}.
 *
 * @since 1.0
 * @see ClientCommandSuggestionsRequestPlayPacket
 * @see NetworkReader
 */
public final class ClientCommandSuggestionsRequestPlayPacketReader
        implements NetworkReader<ClientCommandSuggestionsRequestPlayPacket> {

    /**
     * An instance of the {@linkplain ClientCommandSuggestionsRequestPlayPacket client command suggestions request
     * play packet reader}.
     *
     * @since 1.0
     */
    public static final ClientCommandSuggestionsRequestPlayPacketReader
            INSTANCE = new ClientCommandSuggestionsRequestPlayPacketReader();

    private static final StringNetworkCodec TEXT_CODEC = StringNetworkCodec.create(32_500);

    private ClientCommandSuggestionsRequestPlayPacketReader() {}

    @Override
    public @NonNull ClientCommandSuggestionsRequestPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientCommandSuggestionsRequestPlayPacket(
                VarIntNetworkCodec.INSTANCE.read(buf),
                TEXT_CODEC.read(buf)
        );
    }
}
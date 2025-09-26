package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.PrimitiveNetworkCodecs;
import net.hypejet.jet.server.network.codec.index.IndexNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientChangeDifficultyPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.hypejet.jet.world.difficulty.Difficulty;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientChangeDifficultyPlayPacket a change difficulty play packet}.
 *
 * @since 1.0
 * @see ClientChangeDifficultyPlayPacket
 * @see NetworkReader
 */
public final class ClientChangeDifficultyPlayPacketReader implements NetworkReader<ClientChangeDifficultyPlayPacket> {

    /**
     * An instance of the {@linkplain ClientChangeDifficultyPlayPacketReader client change difficulty play packet
     * reader}.
     *
     * @since 1.0
     */
    public static final ClientChangeDifficultyPlayPacketReader INSTANCE = new ClientChangeDifficultyPlayPacketReader();

    private static final IndexNetworkCodec<Difficulty, Byte> DIFFICULTY_CODEC = new IndexNetworkCodec<>(
            IndexUtil.fromMap(Map.of(
                    (byte) 0, Difficulty.PEACEFUL,
                    (byte) 1, Difficulty.EASY,
                    (byte) 2, Difficulty.NORMAL,
                    (byte) 3, Difficulty.HARD
            )),
            PrimitiveNetworkCodecs.BYTE
    );

    private ClientChangeDifficultyPlayPacketReader() {}

    @Override
    public @NonNull ClientChangeDifficultyPlayPacket read(@NonNull ByteBuf buf,
                                                          @NonNull JetRegistryManager registryManager) {
        return new ClientChangeDifficultyPlayPacket(DIFFICULTY_CODEC.read(buf, registryManager));
    }
}
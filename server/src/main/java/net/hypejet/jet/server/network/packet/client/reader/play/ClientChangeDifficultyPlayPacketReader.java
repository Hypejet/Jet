package net.hypejet.jet.server.network.packet.client.reader.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.network.packet.client.play.ClientChangeDifficultyPlayPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.PrimitiveNetworkCodecs;
import net.hypejet.jet.server.network.codec.mapper.MapperNetworkCodec;
import net.hypejet.jet.world.difficulty.Difficulty;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientChangeDifficultyPlayPacket a change difficulty play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientChangeDifficultyPlayPacket
 * @see NetworkReader
 */
public final class ClientChangeDifficultyPlayPacketReader implements NetworkReader<ClientChangeDifficultyPlayPacket> {

    private static final MapperNetworkCodec<Difficulty, Byte> DIFFICULTY_CODEC = new MapperNetworkCodec<>(
            Mapper.builder(Difficulty.class, byte.class)
                    .register(Difficulty.PEACEFUL, (byte) 0)
                    .register(Difficulty.EASY, (byte) 1)
                    .register(Difficulty.NORMAL, (byte) 2)
                    .register(Difficulty.HARD, (byte) 3)
                    .build(),
            PrimitiveNetworkCodecs.BYTE
    );

    @Override
    public @NonNull ClientChangeDifficultyPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientChangeDifficultyPlayPacket(DIFFICULTY_CODEC.read(buf));
    }
}
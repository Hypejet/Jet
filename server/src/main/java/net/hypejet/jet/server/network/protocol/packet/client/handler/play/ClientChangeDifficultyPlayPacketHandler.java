package net.hypejet.jet.server.network.protocol.packet.client.handler.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.protocol.packet.client.play.ClientChangeDifficultyPlayPacket;
import net.hypejet.jet.server.network.codec.CombinedNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.mapper.MapperNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import net.hypejet.jet.world.difficulty.Difficulty;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientChangeDifficultyPlayPacket a change difficulty play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientChangeDifficultyPlayPacket
 * @see ClientPacketHandler
 */
public final class ClientChangeDifficultyPlayPacketHandler
        implements ClientPacketHandler<ClientChangeDifficultyPlayPacket> {

    private static final MapperNetworkCodec<Difficulty, Byte> DIFFICULTY_CODEC = new MapperNetworkCodec<>(
            Mapper.builder(Difficulty.class, byte.class)
                    .register(Difficulty.PEACEFUL, (byte) 0)
                    .register(Difficulty.EASY, (byte) 1)
                    .register(Difficulty.NORMAL, (byte) 2)
                    .register(Difficulty.HARD, (byte) 3)
                    .build(),
            // TODO
            new CombinedNetworkCodec<>(ByteBuf::readByte, (buf, object) -> buf.writeByte(object))
    );

    @Override
    public @NonNull ClientChangeDifficultyPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientChangeDifficultyPlayPacket(DIFFICULTY_CODEC.read(buf));
    }

    @Override
    public void handle(@NonNull ClientChangeDifficultyPlayPacket packet, @NonNull SessionTask sessionTask) {
        // TODO
    }
}
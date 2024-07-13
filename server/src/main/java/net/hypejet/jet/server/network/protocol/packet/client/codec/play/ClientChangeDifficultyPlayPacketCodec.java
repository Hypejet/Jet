package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientChangeDifficultyPlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.enums.EnumByteCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.world.difficulty.Difficulty;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientChangeDifficultyPlayPacket change difficulty play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientChangeDifficultyPlayPacket
 * @see ClientPacketCodec
 */
public final class ClientChangeDifficultyPlayPacketCodec extends ClientPacketCodec<ClientChangeDifficultyPlayPacket> {

    private static final EnumByteCodec<Difficulty> difficultyCodec = EnumByteCodec.builder(Difficulty.class)
            .add(Difficulty.PEACEFUL, (byte) 0)
            .add(Difficulty.EASY, (byte) 1)
            .add(Difficulty.NORMAL, (byte) 2)
            .add(Difficulty.HARD, (byte) 3)
            .build();

    /**
     * Constructs the {@linkplain ClientChangeDifficultyPlayPacketCodec change difficulty play packet codec}.
     *
     * @since 1.0
     */
    public ClientChangeDifficultyPlayPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_CHANGE_DIFFICULTY, ClientChangeDifficultyPlayPacket.class);
    }

    @Override
    public @NonNull ClientChangeDifficultyPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientChangeDifficultyPlayPacket(difficultyCodec.read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientChangeDifficultyPlayPacket object) {
        difficultyCodec.write(buf, object.difficulty());
    }

    @Override
    public void handle(@NonNull ClientChangeDifficultyPlayPacket packet, @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}
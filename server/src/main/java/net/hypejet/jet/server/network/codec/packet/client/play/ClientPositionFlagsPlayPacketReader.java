package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.game.world.coordinate.PositionFlagsReader;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientPositionFlagsPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientPositionFlagsPlayPacket a client position flags play packet}.
 *
 * @since 1.0
 * @see ClientPositionFlagsPlayPacket
 * @see NetworkReader
 */
public final class ClientPositionFlagsPlayPacketReader implements NetworkReader<ClientPositionFlagsPlayPacket> {

    /**
     * An instance of the {@linkplain ClientPositionFlagsPlayPacketReader client position flags play packet reader}.
     *
     * @since 1.0
     */
    public static final ClientPositionFlagsPlayPacketReader INSTANCE = new ClientPositionFlagsPlayPacketReader();

    private ClientPositionFlagsPlayPacketReader() {}

    @Override
    public @NonNull ClientPositionFlagsPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientPositionFlagsPlayPacket(PositionFlagsReader.INSTANCE.read(buf));
    }
}
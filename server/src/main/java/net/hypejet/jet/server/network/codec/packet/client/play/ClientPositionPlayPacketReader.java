package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.game.world.coordinate.PositionFlagsNetworkReader;
import net.hypejet.jet.server.network.codec.game.world.coordinate.VectorNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientPositionPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads {@linkplain ClientPositionPlayPacket a position
 * play packet}.
 *
 * @since 1.0
 * @see ClientPositionPlayPacket
 * @see NetworkReader
 */
public final class ClientPositionPlayPacketReader implements NetworkReader<ClientPositionPlayPacket> {

    /**
     * An instance of the {@linkplain ClientPositionPlayPacketReader client position play packet reader}.
     *
     * @since 1.0
     */
    public static final ClientPositionPlayPacketReader INSTANCE = new ClientPositionPlayPacketReader();

    private ClientPositionPlayPacketReader() {}

    @Override
    public @NonNull ClientPositionPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientPositionPlayPacket(
                VectorNetworkCodec.INSTANCE.read(buf),
                PositionFlagsNetworkReader.INSTANCE.read(buf)
        );
    }
}
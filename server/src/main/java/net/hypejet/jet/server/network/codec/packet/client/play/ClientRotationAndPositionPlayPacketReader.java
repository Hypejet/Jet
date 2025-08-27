package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.game.world.coordinate.position.PositionFlagsNetworkReader;
import net.hypejet.jet.server.network.codec.game.world.coordinate.position.PositionNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientRotationAndPositionPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientRotationAndPositionPlayPacket a rotation and position play packet}.
 *
 * @since 1.0
 * @see ClientRotationAndPositionPlayPacket
 * @see NetworkReader
 */
public final class ClientRotationAndPositionPlayPacketReader
        implements NetworkReader<ClientRotationAndPositionPlayPacket> {

    /**
     * An instance of the {@linkplain ClientRotationAndPositionPlayPacketReader client rotation and position play
     * packet reader}.
     *
     * @since 1.0
     */
    public static final ClientRotationAndPositionPlayPacketReader
            INSTANCE = new ClientRotationAndPositionPlayPacketReader();

    private ClientRotationAndPositionPlayPacketReader() {}

    @Override
    public @NonNull ClientRotationAndPositionPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientRotationAndPositionPlayPacket(
                PositionNetworkCodec.INSTANCE.read(buf),
                PositionFlagsNetworkReader.INSTANCE.read(buf)
        );
    }
}
package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.game.world.coordinate.PositionFlagsNetworkReader;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientRotationPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads {@linkplain ClientRotationPlayPacket a rotation
 * play packet}.
 *
 * @since 1.0
 * @see ClientRotationPlayPacket
 * @see NetworkReader
 */
public final class ClientRotationPlayPacketReader implements NetworkReader<ClientRotationPlayPacket> {

    /**
     * An instance of the {@linkplain ClientRotationPlayPacketReader client rotation play packet reader}.
     *
     * @since 1.0
     */
    public static final ClientRotationPlayPacketReader INSTANCE = new ClientRotationPlayPacketReader();

    private ClientRotationPlayPacketReader() {}

    @Override
    public @NonNull ClientRotationPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientRotationPlayPacket(
                buf.readFloat(), buf.readFloat(),
                PositionFlagsNetworkReader.INSTANCE.read(buf)
        );
    }
}
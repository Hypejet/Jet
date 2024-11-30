package net.hypejet.jet.server.network.packet.client.reader.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.client.play.ClientRotationAndPositionPlayPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.game.world.coordinate.PositionFlagsReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientRotationAndPositionPlayPacket a rotation and position play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientRotationAndPositionPlayPacket
 * @see NetworkReader
 */
public final class ClientRotationAndPositionPlayPacketReader
        implements NetworkReader<ClientRotationAndPositionPlayPacket> {
    @Override
    public @NonNull ClientRotationAndPositionPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientRotationAndPositionPlayPacket(
                buf.readDouble(), buf.readDouble(), buf.readDouble(),
                buf.readFloat(), buf.readFloat(), PositionFlagsReader.INSTANCE.read(buf)
        );
    }
}
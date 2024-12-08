package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientRotationPlayPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.game.world.coordinate.PositionFlagsReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads {@linkplain ClientRotationPlayPacket a rotation
 * and position play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientRotationPlayPacket
 * @see NetworkReader
 */
public final class ClientRotationPlayPacketReader implements NetworkReader<ClientRotationPlayPacket> {
    @Override
    public @NonNull ClientRotationPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientRotationPlayPacket(
                buf.readFloat(), buf.readFloat(),
                PositionFlagsReader.INSTANCE.read(buf)
        );
    }
}
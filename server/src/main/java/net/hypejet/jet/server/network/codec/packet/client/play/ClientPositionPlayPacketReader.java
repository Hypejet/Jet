package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientPositionPlayPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.game.world.coordinate.PositionFlagsReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads {@linkplain ClientPositionPlayPacket a position
 * play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPositionPlayPacket
 * @see NetworkReader
 */
public final class ClientPositionPlayPacketReader implements NetworkReader<ClientPositionPlayPacket> {
    @Override
    public @NonNull ClientPositionPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientPositionPlayPacket(
                buf.readDouble(), buf.readDouble(), buf.readDouble(),
                PositionFlagsReader.INSTANCE.read(buf)
        );
    }
}
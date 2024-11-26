package net.hypejet.jet.server.network.protocol.packet.client.handler.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientRotationAndPositionPlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.game.world.coordinate.PositionFlagsReader;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientRotationAndPositionPlayPacket a rotation and position play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientRotationAndPositionPlayPacket
 * @see ClientPacketHandler
 */
public final class ClientRotationAndPositionPlayPacketHandler
        implements ClientPacketHandler<ClientRotationAndPositionPlayPacket> {
    @Override
    public @NonNull ClientRotationAndPositionPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientRotationAndPositionPlayPacket(
                buf.readDouble(), buf.readDouble(), buf.readDouble(),
                buf.readFloat(), buf.readFloat(), PositionFlagsReader.INSTANCE.read(buf)
        );
    }

    @Override
    public void handle(@NonNull ClientRotationAndPositionPlayPacket packet,
                       @NonNull SessionTask sessionTask) {
        // TODO
    }
}
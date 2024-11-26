package net.hypejet.jet.server.network.protocol.packet.client.handler.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientPositionPlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.game.world.coordinate.PositionFlagsReader;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientPositionPlayPacket a position play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPositionPlayPacket
 * @see ClientPacketHandler
 */
public final class ClientPositionPlayPacketHandler implements ClientPacketHandler<ClientPositionPlayPacket> {
    @Override
    public @NonNull ClientPositionPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientPositionPlayPacket(
                buf.readDouble(), buf.readDouble(), buf.readDouble(),
                PositionFlagsReader.INSTANCE.read(buf)
        );
    }

    @Override
    public void handle(@NonNull ClientPositionPlayPacket packet, @NonNull SessionTask sessionTask) {
        // TODO
    }
}
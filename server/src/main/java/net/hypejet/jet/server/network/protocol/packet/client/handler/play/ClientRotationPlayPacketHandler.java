package net.hypejet.jet.server.network.protocol.packet.client.handler.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientRotationPlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.coordinate.PositionFlagsReader;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientRotationPlayPacket a rotation and position play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientRotationPlayPacket
 * @see ClientPacketHandler
 */
public final class ClientRotationPlayPacketHandler implements ClientPacketHandler<ClientRotationPlayPacket> {
    @Override
    public @NonNull ClientRotationPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientRotationPlayPacket(
                buf.readFloat(), buf.readFloat(),
                PositionFlagsReader.INSTANCE.read(buf)
        );
    }

    @Override
    public void handle(@NonNull ClientRotationPlayPacket packet, @NonNull SessionTask sessionTask) {
        // TODO
    }
}
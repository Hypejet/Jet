package net.hypejet.jet.protocol.packet.server;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.play.ServerDisconnectPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerKeepAlivePlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerPacket server packet}, which can be sent during
 * a {@linkplain ProtocolState#PLAY play protocol state} only.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 */
public sealed interface ServerPlayPacket extends ServerPacket permits ServerDisconnectPlayPacket,
        ServerKeepAlivePlayPacket {
    /**
     * {@inheritDoc}
     */
    @Override
    default @NonNull ProtocolState state() {
        return ProtocolState.PLAY;
    }
}
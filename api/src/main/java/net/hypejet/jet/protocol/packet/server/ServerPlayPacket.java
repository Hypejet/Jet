package net.hypejet.jet.protocol.packet.server;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.play.ServerActionBarPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerCenterChunkPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerChunkAndLightDataPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerDisconnectPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerGameEventPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerJoinGamePlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerKeepAlivePlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerPlayerListHeaderAndFooterPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerPluginMessagePlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerSynchronizePositionPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerSystemMessagePlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerPacket server packet}, which can be sent during
 * a {@linkplain ProtocolState#PLAY play protocol state} only.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 */
public sealed interface ServerPlayPacket extends ServerPacket permits ServerActionBarPlayPacket,
        ServerCenterChunkPlayPacket, ServerChunkAndLightDataPlayPacket, ServerDisconnectPlayPacket,
        ServerGameEventPlayPacket, ServerJoinGamePlayPacket, ServerKeepAlivePlayPacket,
        ServerPlayerListHeaderAndFooterPlayPacket, ServerPluginMessagePlayPacket, ServerSynchronizePositionPlayPacket,
        ServerSystemMessagePlayPacket {
    /**
     * {@inheritDoc}
     */
    @Override
    default @NonNull ProtocolState state() {
        return ProtocolState.PLAY;
    }
}
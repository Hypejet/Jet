package net.hypejet.jet.protocol.packet.client;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.play.ClientAcknowledgeMessagePlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientActionPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientChangeDifficultyPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientChatCommandPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientChatSessionUpdatePlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientCommandSuggestionsRequestPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientConfirmTeleportationPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientInformationPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientKeepAlivePlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientRequestActionPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientOnGroundPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientPluginMessagePlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientPositionPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientQueryBlockEntityTagPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientRotationAndPositionPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientRotationPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientSignedChatCommandPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientSignedChatMessagePlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacket client packet}, which can be received during
 * a {@linkplain ProtocolState#PLAY play protocol state}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public sealed interface ClientPlayPacket extends ClientPacket permits ClientAcknowledgeMessagePlayPacket,
        ClientActionPlayPacket, ClientChangeDifficultyPlayPacket, ClientChatCommandPlayPacket,
        ClientChatSessionUpdatePlayPacket, ClientCommandSuggestionsRequestPlayPacket,
        ClientConfirmTeleportationPlayPacket, ClientInformationPlayPacket, ClientKeepAlivePlayPacket,
        ClientOnGroundPlayPacket, ClientPluginMessagePlayPacket, ClientPositionPlayPacket,
        ClientQueryBlockEntityTagPacket, ClientRequestActionPlayPacket, ClientRotationAndPositionPlayPacket,
        ClientRotationPlayPacket, ClientSignedChatCommandPlayPacket, ClientSignedChatMessagePlayPacket {
    /**
     * {@inheritDoc}
     */
    @Override
    default @NonNull ProtocolState state() {
        return ProtocolState.PLAY;
    }
}
package net.hypejet.jet.protocol.packet.client;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.login.ClientCookieResponseLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientEncryptionResponseLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginAcknowledgeLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginRequestLoginPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientPluginMessageResponseLoginPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacket client packet}, which can be received during
 * a {@linkplain net.hypejet.jet.protocol.ProtocolState#LOGIN login protocol state}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public sealed interface ClientLoginPacket extends ClientPacket permits ClientCookieResponseLoginPacket,
        ClientEncryptionResponseLoginPacket, ClientLoginAcknowledgeLoginPacket, ClientLoginRequestLoginPacket,
        ClientPluginMessageResponseLoginPacket {
    /**
     * {@inheritDoc}
     */
    @Override
    default @NonNull ProtocolState state() {
        return ProtocolState.LOGIN;
    }
}
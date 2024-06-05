package net.hypejet.jet.protocol.packet.server;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.login.ServerCookieRequestPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerDisconnectPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerEnableCompressionPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerEncryptionRequestPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerLoginSuccessPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerPluginMessageRequestPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerPacket server packet}, which can be sent during a {@linkplain ProtocolState#LOGIN
 * login protocol state} only.
 * 
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 */
public sealed interface ServerLoginPacket extends ServerPacket permits ServerEnableCompressionPacket,
        ServerCookieRequestPacket, ServerDisconnectPacket, ServerEncryptionRequestPacket,
        ServerPluginMessageRequestPacket, ServerLoginSuccessPacket {
    /**
     * {@inheritDoc}
     */
    @Override
    default @NonNull ProtocolState state() {
        return ProtocolState.LOGIN;
    }
}
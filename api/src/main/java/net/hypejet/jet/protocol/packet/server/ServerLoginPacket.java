package net.hypejet.jet.protocol.packet.server;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.login.ServerCookieRequestLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerDisconnectLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerEnableCompressionLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerEncryptionRequestLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerLoginSuccessLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerPluginMessageRequestLoginPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerPacket server packet}, which can be sent during a {@linkplain ProtocolState#LOGIN
 * login protocol state} only.
 * 
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 */
public sealed interface ServerLoginPacket extends ServerPacket permits ServerEnableCompressionLoginPacket,
        ServerCookieRequestLoginPacket, ServerDisconnectLoginPacket, ServerEncryptionRequestLoginPacket,
        ServerPluginMessageRequestLoginPacket, ServerLoginSuccessLoginPacket {
    /**
     * {@inheritDoc}
     */
    @Override
    default @NonNull ProtocolState state() {
        return ProtocolState.LOGIN;
    }
}
package net.hypejet.jet.protocol.packet.server.login;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.protocol.packet.server.login.compression.ServerEnableCompressionPacket;
import net.hypejet.jet.protocol.packet.server.login.cookie.ServerCookieRequestPacket;
import net.hypejet.jet.protocol.packet.server.login.disconnect.ServerDisconnectPacket;
import net.hypejet.jet.protocol.packet.server.login.encryption.ServerEncryptionRequestPacket;
import net.hypejet.jet.protocol.packet.server.login.plugin.ServerPluginMessageRequestPacket;
import net.hypejet.jet.protocol.packet.server.login.success.ServerLoginSuccessPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerPacket server packet} with a {@linkplain ProtocolState#LOGIN login protocol state}.
 * 
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 */
public sealed interface ServerLoginPacket extends ServerPacket permits ServerEnableCompressionPacket,
        ServerCookieRequestPacket, ServerDisconnectPacket, ServerEncryptionRequestPacket,
        ServerPluginMessageRequestPacket, ServerLoginSuccessPacket {
    @Override
    default @NonNull ProtocolState getProtocolState() {
        return ProtocolState.LOGIN;
    }
}
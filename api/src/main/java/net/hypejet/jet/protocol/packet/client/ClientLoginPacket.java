package net.hypejet.jet.protocol.packet.client;

import net.hypejet.jet.protocol.packet.client.login.ClientCookieResponsePacket;
import net.hypejet.jet.protocol.packet.client.login.ClientEncryptionResponsePacket;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginAcknowledgePacket;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginRequestPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientPluginMessageResponsePacket;

/**
 * Represents a {@linkplain  ClientPacket client packet}, which can be sent in
 * {@linkplain  net.hypejet.jet.protocol.ProtocolState#LOGIN login protocol state}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public sealed interface ClientLoginPacket extends ClientPacket permits ClientCookieResponsePacket,
        ClientEncryptionResponsePacket, ClientLoginAcknowledgePacket, ClientLoginRequestPacket,
        ClientPluginMessageResponsePacket {}
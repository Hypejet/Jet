package net.hypejet.jet.server.test.network.protocol.packet.server;

import io.netty.buffer.Unpooled;
import net.hypejet.jet.player.profile.properties.GameProfileProperties;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerEnableCompressionPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerCookieRequestPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerDisconnectPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerEncryptionRequestPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerPluginMessageRequestPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerLoginSuccessPacket;
import net.hypejet.jet.server.network.protocol.codecs.GameProfilePropertiesCodec;
import net.hypejet.jet.server.network.protocol.ServerPacketRegistry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a test for {@link ServerPacketRegistry server packet registry}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacketRegistry
 */
public final class ServerPacketRegistryTest {
}
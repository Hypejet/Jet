package net.hypejet.jet.protocol.packet.server.login;

import net.hypejet.jet.protocol.packet.server.ServerLoginPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerLoginPacket server login packet} sent by a server to disconnect a player.
 *
 * @param reason a reason of the disconnection
 * @since 1.0
 * @author Codestech
 * @see ServerLoginPacket
 */
public record ServerDisconnectLoginPacket(@NonNull Component reason) implements ServerLoginPacket {}
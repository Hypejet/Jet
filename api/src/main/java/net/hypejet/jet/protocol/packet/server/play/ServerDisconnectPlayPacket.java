package net.hypejet.jet.protocol.packet.server.play;

import net.hypejet.jet.protocol.packet.server.ServerPlayPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerPlayPacket server play packet} sent by a server to disconnect a player.
 *
 * @param reason a reason of the disconnection
 * @since 1.0
 * @author Codestech
 * @see ServerPlayPacket
 */
public record ServerDisconnectPlayPacket(@NonNull Component reason) implements ServerPlayPacket {}
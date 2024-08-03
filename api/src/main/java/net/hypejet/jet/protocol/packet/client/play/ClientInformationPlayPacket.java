package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet} updating settings of a player on a server.
 *
 * @param settings the settings
 * @since 1.0
 * @author Codestech
 * @see ClientPlayPacket
 */
public record ClientInformationPlayPacket(Player.@NonNull Settings settings) implements ClientPlayPacket {}
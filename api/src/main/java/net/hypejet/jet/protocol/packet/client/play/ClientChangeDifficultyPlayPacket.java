package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;
import net.hypejet.jet.world.difficulty.Difficulty;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which is received when a client wants to change
 * a difficulty.
 *
 * <p>This packet has no usage on a server. It is used only on Minecraft single-player. We use this only for throwing
 * an exception due to a packet, which cannot be sent.</p>
 *
 * @param difficulty the new difficulty
 * @since 1.0
 * @author Codestech
 */
public record ClientChangeDifficultyPlayPacket(@NonNull Difficulty difficulty) implements ClientPlayPacket {}